package com.chunlei.bili.search.service.Impl;

import com.chunlei.bili.search.dto.OperationDTO;
import com.chunlei.bili.search.dto.VideoDTO;
import com.chunlei.bili.search.entity.EsVideo;
import com.chunlei.bili.search.mapper.MemberVideoPreferenceMapper;
import com.chunlei.bili.search.model.MemberVideoPreference;
import com.chunlei.bili.search.service.EsSearchService;
import com.chunlei.bili.search.service.RecommendService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    MemberVideoPreferenceMapper memberVideoPreferenceMapper;
    @Autowired
    DataModel dataModel;
    @Autowired
    EsSearchService searchService;

    @Override
    public void addOperation(OperationDTO operationDTO) {
        MemberVideoPreference preference = memberVideoPreferenceMapper.selectByPrimaryKey(operationDTO.getMemberId(), operationDTO.getVideoId());
        if (preference == null){
            if (operationDTO.getOpt() != 1){
                return;
            }
            preference = new MemberVideoPreference();
            preference.setView((byte) 1);
            preference.setMemberId(operationDTO.getMemberId());
            preference.setVideoId(operationDTO.getVideoId());
            preference.setScore(6);
            preference.setTime(new Date());
            memberVideoPreferenceMapper.insertSelective(preference);
        }
        Integer opt = operationDTO.getOpt();
        switch (opt){
            case 1: return;
            case 2: {
                if (preference.getLike() != 1){
                    preference.setLike((byte) 1);
                    preference.setScore(preference.getScore()+2);
                    preference.setTime(new Date());
                    memberVideoPreferenceMapper.updateByPrimaryKeySelective(preference);
                }
            }
            case 3: {
                if (preference.getReply() != 1){
                    preference.setReply((byte) 1);
                    preference.setScore(preference.getScore()+2);
                    preference.setTime(new Date());
                    memberVideoPreferenceMapper.updateByPrimaryKeySelective(preference);
                }
            }
        }
    }

    @Override
    public List<VideoDTO> getRecommendVideoByUser(Long memberId, Integer ps) {

        List<VideoDTO> videos = null;

        try {
            UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100,similarity,dataModel);
            Recommender recommender = new GenericUserBasedRecommender(dataModel,userNeighborhood,similarity);
            List<RecommendedItem> recommend = recommender.recommend(memberId, ps);
            List<Long> videoIdList = recommend.stream().map(RecommendedItem::getItemID).collect(Collectors.toList());
            videos = searchService.findVideoByIds(videoIdList);

        } catch (TasteException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return videos;
    }

    @Override
    public List<VideoDTO> getRecommendVideoByItem(Long memberId, Long videoId, Integer ps) {

        List<VideoDTO> videos = null;

        try {
            ItemSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel,similarity);
            List<RecommendedItem> recommend = recommender.recommendedBecause(memberId,videoId, ps);
            List<Long> videoIdList = recommend.stream().map(RecommendedItem::getItemID).collect(Collectors.toList());
            videos = searchService.findVideoByIds(videoIdList);
        } catch (TasteException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return videos;
    }
}
