package com.chunlei.bili.danmaku.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chunlei.bili.common.utils.JwtUtils;
import com.chunlei.bili.danmaku.component.KafkaProducer;
import com.chunlei.bili.danmaku.config.KafkaConfig;
import com.chunlei.bili.danmaku.model.Danmaku;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.channels.Channel;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.chunlei.bili.common.constant.WebConstants.TOKEN_HEAD;

@Component
@ServerEndpoint("/imserver/{token}")
@Slf4j
public class WebSocketService {



    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * 通用的上下文环境变量的方法，每个WebSocketService都会共用同一个ApplicationContext
     *
     * @param applicationContext
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketService.APPLICATION_CONTEXT = applicationContext;
    }

    //TODO:将在线人数统计放入redis，实现集群统计

    /**
     * 当前长连接的数量（在线人数的统计）
     * 也就是当前有多少客户端通过WebSocket连接到服务端
     */
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    /**
     * 一个客户端 关联 一个WebSocketService
     * 是一个多例模式的，这里需要注意下
     */
    public static final ConcurrentHashMap<String, WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();

    /**
     * 服务端 和 客户端 进行通信的一个会话
     * 当我们有一个客户端进来了，然后保持连接成功了，那么我们就会保存一个跟这个客户端关联的session
     */
    private Session session;

    /**
     * 唯一标识
     */
    private String sessionId;

    private Long memberId;

    /**
     * 打开链接
     * @param session
     * @OnOpen 连接成功后会自动调用这个方法
     */
    @OnOpen
    public void openConnection(Session session, @PathParam("token") String token){
        // 如果是游客观看视频，虽然有弹幕，但是没有用户信息，所以需要用try
        try {
            token = token.substring(TOKEN_HEAD.length());
            this.memberId = Long.valueOf(JwtUtils.getId(token));
        } catch (Exception ignored){
        }
        this.session = session;
        this.sessionId = session.getId();

        //判断WEBSOCKET_MAP是否含有sessionId,有的话删除仔重新添加
        if (WEBSOCKET_MAP.containsKey(sessionId)){
            WEBSOCKET_MAP.remove(sessionId);
            WEBSOCKET_MAP.put(sessionId,this);
        } else {
            WEBSOCKET_MAP.put(sessionId,this);
            ONLINE_COUNT.getAndIncrement();
        }
        log.info("用户连接成功：" + sessionId + "，当前在线人数为：" + ONLINE_COUNT.get());

        //连接成功之后需要通知客户端，方便客户端进行后续操作
        try {
            sendMessage("ok");
        } catch (IOException e) {
            log.error("连接异常！");
        }
    }

    /**
     * 客户端刷新页面，或者关闭页面，服务端断开连接等等操作，都需要关闭连接
     */
    @OnClose
    public void closeConnection() {
        if (WEBSOCKET_MAP.containsKey(sessionId)) {
            WEBSOCKET_MAP.remove(sessionId);
            // 在线人数减一
            ONLINE_COUNT.getAndDecrement();
            log.info("用户退出：" + sessionId + "，当前在线人数为：" + ONLINE_COUNT.get());
        }
    }

    /**
     * 客户端发送消息给后端
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("用户信息：" + sessionId + "，报文：" + message);
        if (!StrUtil.isEmpty(message)){
            try {
                for (Map.Entry<String, WebSocketService> entry : WEBSOCKET_MAP.entrySet()) {
                    WebSocketService webSocketService = entry.getValue();
                    if (webSocketService.session.isOpen()){
                        //webSocketService.sendMessage(message);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("message", message);
                        jsonObject.put("sessionId", webSocketService.sessionId);
                        KafkaProducer kafkaProducer = (KafkaProducer) APPLICATION_CONTEXT.getBean("kafkaProducer");
                        kafkaProducer.sendMessage(KafkaConfig.SEND_TOPIC, jsonObject.toJSONString(),System.currentTimeMillis(),null);
                    }
                }
                if (this.memberId != null){
                    //保存弹幕到数据库
                    Danmaku danmaku = JSON.parseObject(message,Danmaku.class);
                    danmaku.setMemberId(memberId);
                    danmaku.setCreateTime(new Date());
                    DanmakuService danmakuService = (DanmakuService) APPLICATION_CONTEXT.getBean("danmakuService");
                    danmakuService.addDanmaku(danmaku);
                    danmakuService.addDanmakuToRedis(danmaku);
                }
            } catch (Exception e)
            {
                log.error("弹幕接收出现问题！");
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误之后的处理
     *
     * @param error
     */
    @OnError
    public void onError(Throwable error) {

    }

    /**
     * 后端发送消息给客户端
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public Session getSession() {
        return this.session;
    }
}
