package com.zhu.rimxia.biz.common;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/websocket/{userId}")
@Component
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet =
            new CopyOnWriteArraySet<MyWebSocket>();

    public static Map routeTable = new HashMap<String,Session>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    @OnOpen
    public void onOpen(@PathParam("userId")String userId, Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        routeTable.put(userId, session);
        sendMessageTo("你好",userId);
        System.out.println("有新连接加入"+userId+"！当前在线人数为" + onlineCount.incrementAndGet());//在线数加1
//        try {
//            this.sendMessage("hello");
//        } catch (IOException e) {
//            System.out.println("IO异常");
//        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
                   //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + onlineCount.decrementAndGet());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端"+session.getId()+"的消息:" + message);

        //群发消息
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生错误时调用
     @OnError
     */
     public void onError(Session session, Throwable error) {
         System.out.println("发生错误");
         error.printStackTrace();
     }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public void sendMessageTo(String message,String userId){
         Session session = (Session)routeTable.get(userId);
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
