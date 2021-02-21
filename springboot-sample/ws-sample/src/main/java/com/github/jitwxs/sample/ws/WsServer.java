package com.github.jitwxs.sample.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * WebSocket HelloWorld
 * @author jitwxs
 * @since 2018/10/10 9:41
 */
@ServerEndpoint(value = "/ws")
@Component
public class WsServer {
    private Logger log  = LoggerFactory.getLogger(this.getClass());

    /**
     * 用来存放每个客户端对应的MyWebSocket对象
     */
    private static CopyOnWriteArraySet<WsServer> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功
     * @author jitwxs
     * @since 2018/10/10 9:44
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        log.info("【WebSocket】客户端：{} 加入连接！当前在线人数为：{}", session.getId(), webSocketSet.size());

        sendMessage("已接受您的连接请求");
    }

    /**
     * 连接关闭
     * @author jitwxs
     * @since 2018/10/10 9:45
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("【WebSocket】客户端：{} 关闭连接！当前在线人数为：{}", this.session.getId(), webSocketSet.size());

    }

    /**
     * 收到客户端消息
     * @author jitwxs
     * @since 2018/10/10 9:45
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("【WebSocket】收到来自客户端：{} 的消息，消息内容：{}", session.getId(), message);
        sendMessage("收到消息：" + message);
    }

    /**
     * 发生错误
     * @author jitwxs
     * @since 2018/10/10 9:46
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("【WebSocket】客户端：{} 发生错误，错误信息：", session.getId(), error);
    }


    /**
     * 对当前客户端发送消息
     * @author jitwxs
     * @since 2018/10/10 9:49
     */
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
//        this.session.getBasicRemote().sendText(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WsServer that = (WsServer) o;
        return Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session);
    }
}
