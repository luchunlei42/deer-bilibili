package com.chunlei.bili.reply.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.chunlei.bili.common.api.R;
import com.chunlei.bili.common.utils.UserHolder;
import com.chunlei.bili.reply.client.MemberFeignClient;
import com.chunlei.bili.reply.client.VideoFeignClient;
import com.chunlei.bili.reply.component.KafkaProducer;
import com.chunlei.bili.reply.config.KafkaConfig;
import com.chunlei.bili.reply.config.RedisConfig;
import com.chunlei.bili.reply.dao.ReplyDao;
import com.chunlei.bili.reply.dto.MemberInfo;
import com.chunlei.bili.reply.dto.ReplyDTO;
import com.chunlei.bili.reply.dto.ReplyResult;
import com.chunlei.bili.reply.mapper.ReplyMapper;
import com.chunlei.bili.reply.model.Reply;
import com.chunlei.bili.reply.model.ReplyExample;
import com.chunlei.bili.reply.service.ReplyService;
import com.chunlei.bili.video.model.Video;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    VideoFeignClient videoFeignClient;
    @Autowired
    KafkaProducer kafkaProducer;
    @Autowired
    ReplyMapper replyMapper;
    @Autowired
    MemberFeignClient memberFeignClient;
    @Autowired
    ExecutorService childrenReplyThreadPool;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ReplyDao replyDao;

    @Override
    public void postReply(Reply reply) {
        Long videoId = reply.getVideoId();
        //判断video是否存在
        R<Video> videoInfo = videoFeignClient.getVideoInfo(videoId);
        if (videoInfo.getCode() != 200){
            throw new RuntimeException("视频不存在");
        }
        Long memberId = UserHolder.getUser().getId();
        reply.setMemberId(memberId);
        reply.setCreateTime(new Date());
        reply.setUpdateTime(new Date());
        reply.setStatus(2);
        //发送kafka消息异步处理
        String jsonString = JSON.toJSONString(reply);
        kafkaProducer.sendMessage(KafkaConfig.REPLY_TOPIC,jsonString,null,null);
    }

    @Override
    public ReplyResult getReplyMain(Long videoId, Integer ps, Integer pn, Integer cursor) throws ExecutionException, InterruptedException {
        String replyIndexKey = RedisConfig.REPLY_INDEX_PREFIX+videoId;
        if (redisTemplate.expire(replyIndexKey,RedisConfig.REPLY_INDEX_EXPIRE,TimeUnit.HOURS)){
            ReplyResult replyResult = new ReplyResult();
            //从redis获取指定分页的数据
            if (cursor == null){
                cursor = Math.toIntExact(redisTemplate.opsForZSet().zCard(replyIndexKey))-1;
            }
            int next = cursor - ps;
            if(next < -1){
                next = -1;
                replyResult.setIsEnd(true);
            }
            replyResult.setNext(next);
            replyResult.setSize(ps);
            replyResult.setNum(pn);
            Set<String> range = redisTemplate.opsForZSet().range(replyIndexKey, next + 1, cursor);
            if (range == null || range.size() < 0){
                return replyResult;
            }

            List<String> replyIds = range.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            List<ReplyDTO> replies = getReplyByIds(replyIds);
            replyResult.setReplies(replies);
            return replyResult;

        }else{
            //key不存在，从db中拿数据，再放入redis,看来只在开始有效果
            ReplyResult replyResult = loadFullReplies(videoId, ps, 1);
            return replyResult;
        }
    }

    private List<ReplyDTO> getReplyByIds(List<String> replyIds) throws ExecutionException, InterruptedException {
        //先从从redis取
        List<ReplyDTO> res = new ArrayList<>();
        List<String> redisKeys = replyIds.stream().map(id->RedisConfig.REPLY_CONTENT_PREFIX+id).collect(Collectors.toList());
        List<String> list = redisTemplate.opsForValue().multiGet(redisKeys);
        List<String> missedIds = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            String s = list.get(i);
            if (StrUtil.isEmpty(s)){
                //缓存未命中，去mysql查询并写回redis
//                String redisKey = redisKeys.get(i);
                String replyId = replyIds.get(i);
                missedIds.add(replyId);
            } else {
                ReplyDTO replyDTO = JSON.parseObject(s, ReplyDTO.class);
                res.add(replyDTO);
            }
        }
        if (!missedIds.isEmpty()){
            List<ReplyDTO> replyDTOList = loadRepliesFromDB(missedIds);
            res.addAll(replyDTOList);
        }
        res.sort(Comparator.comparing(ReplyDTO::getTime).reversed());
        return res;
    }

    private List<ReplyDTO> loadRepliesFromDB(List<String> missedIds) throws ExecutionException, InterruptedException {
        List<Long> ids = missedIds.stream().map(id->Long.parseLong(id)).collect(Collectors.toList());
        ReplyExample replyExample = new ReplyExample();
        replyExample.createCriteria().andIdIn(ids);
        List<Reply> replies = replyMapper.selectByExample(replyExample);
        List<ReplyDTO> replyDTOList = mapReplyToReplyDTO(replies);
        //3.将该评论存入redis
        saveRedisReplyIndexSortedByTime(replyDTOList);
        return replyDTOList;
    }

    private List<ReplyDTO> mapReplyToReplyDTO(List<Reply> replies) throws ExecutionException, InterruptedException {
        //2.并发查询楼中楼评论列表
        List<CompletableFuture<ReplyDTO>> futureList = replies.stream().map(reply -> getReplyDTOFuture(reply)).collect(Collectors.toList());
        CompletableFuture<Void> all = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        CompletableFuture<List<ReplyDTO>> listCompletableFuture = all.thenApply(v -> futureList.stream().map(a -> {
            try {
                return a.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()));
        List<ReplyDTO> replyDTOList = listCompletableFuture.get();
        return replyDTOList;
    }


    private ReplyResult loadFullReplies(Long videoId, Integer ps, Integer pn) throws ExecutionException, InterruptedException {
        //1.批量查询根评论基础信息
        ReplyResult replyResult = new ReplyResult();
        replyResult.setNum(pn);
        replyResult.setSize(ps);
        PageHelper.startPage(pn,ps);
        ReplyExample replyExample= new ReplyExample();
        replyExample.createCriteria().andVideoIdEqualTo(videoId).andRootIdEqualTo(0L).andStatusEqualTo(2);
        replyExample.setOrderByClause("create_time DESC");
        List<Reply> replies = replyMapper.selectByExample(replyExample);
        List<ReplyDTO> replyDTOList = mapReplyToReplyDTO(replies);
        replyResult.setReplies(replyDTOList);

        //3.将该评论存入redis
        saveRedisReplyIndexSortedByTime(replyDTOList);
        //4.重建评论列表
        rebuildReplyListCache(videoId);

        return replyResult;
    }

    /**
     * 在redis重建评论索引列表
     * @param videoId
     */
    private void rebuildReplyListCache(Long videoId) {
        List<Reply> replyList = replyDao.getRootRepliesWithTime(videoId);
        if (replyList == null || replyList.isEmpty()){
            return;
        }
        Set<ZSetOperations.TypedTuple<String>> collect = replyList.stream().map(reply -> {
            ZSetOperations.TypedTuple<String> tuple = new DefaultTypedTuple<>(reply.getId().toString(), (double) reply.getCreateTime().getTime());
            return tuple;
        }).collect(Collectors.toSet());
        String replyIndexKey = RedisConfig.REPLY_INDEX_PREFIX+videoId;
        redisTemplate.opsForZSet().add(replyIndexKey, collect);
        redisTemplate.expire(replyIndexKey,RedisConfig.REPLY_INDEX_EXPIRE, TimeUnit.HOURS);
    }

    private void saveRedisReplyIndexSortedByTime(List<ReplyDTO> replyDTOList){
        if (replyDTOList == null || replyDTOList.isEmpty()) {
            return;
        }

//        Set<ZSetOperations.TypedTuple<String>> typedTuples = new HashSet<>();
        Map<String,String> map = new HashMap<>();
        for (ReplyDTO replyDTO : replyDTOList) {
//            ZSetOperations.TypedTuple<String> tuple = new DefaultTypedTuple<>(replyDTO.getRpid().toString(), (double) replyDTO.getTime().getTime());
//            typedTuples.add(tuple);
            String replyContentKey = RedisConfig.REPLY_CONTENT_PREFIX+replyDTO.getRpid();
            String jsonString = JSON.toJSONString(replyDTO);
            map.put(replyContentKey,jsonString);
        }
//        String replyIndexKey = RedisConfig.REPLY_INDEX_PREFIX+replyDTOList.get(0).getVid();
//        redisTemplate.opsForZSet().add(replyIndexKey, typedTuples);
//        redisTemplate.expire(replyIndexKey,RedisConfig.REPLY_INDEX_EXPIRE, TimeUnit.HOURS);


        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            map.forEach((key, value) -> {
                connection.set(serializer.serialize(key), serializer.serialize(value),
                        Expiration.seconds(RedisConfig.REPLY_CONTENT_EXPIRE*3600),
                        RedisStringCommands.SetOption.UPSERT);
            });
            return null;
        }, serializer);

    }

    private CompletableFuture<ReplyDTO> getReplyDTOFuture(Reply reply){
        CompletableFuture<ReplyDTO> replyDTOCompletableFuture = CompletableFuture.supplyAsync(() -> {
            ReplyDTO replyDTO = new ReplyDTO();
            replyDTO.setVid(reply.getVideoId());
            replyDTO.setRpid(reply.getId());
            replyDTO.setContent(reply.getContent());
            replyDTO.setTime(reply.getUpdateTime());
            ReplyExample replyExampleForChild = new ReplyExample();
            replyExampleForChild.createCriteria().andRootIdEqualTo(reply.getId()).andStatusEqualTo(2);
            PageHelper.startPage(1, 3);
            List<Reply> replyList = replyMapper.selectByExample(replyExampleForChild);
            List<ReplyDTO> children = replyList.stream().map(item -> {
                ReplyDTO child = new ReplyDTO();
                child.setVid(item.getVideoId());
                child.setRpid(item.getId());
                child.setContent(item.getContent());
                R<MemberInfo> infoR = memberFeignClient.memberInfo(item.getMemberId());
                if (infoR.getCode() == 200) {
                    child.setMemberInfo(infoR.getData());
                }
                child.setTime(item.getCreateTime());
                return child;
            }).collect(Collectors.toList());
            replyDTO.setReplies(children);
            R<MemberInfo> infoR = memberFeignClient.memberInfo(reply.getMemberId());
            if (infoR.getCode() == 200){
                replyDTO.setMemberInfo(infoR.getData());
            }
            return replyDTO;
        },childrenReplyThreadPool);
        return replyDTOCompletableFuture;
    }
}
