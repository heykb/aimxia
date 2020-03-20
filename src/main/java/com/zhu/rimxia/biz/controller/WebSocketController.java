package com.zhu.rimxia.biz.controller;


import com.zhu.rimxia.biz.common.MyWebSocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.websocket.Session;
import java.io.IOException;

@RestController
@RequestMapping("/webSocket")
public class WebSocketController {


//    @Resource
//    private MyWebSocket myWebSocket;

    @GetMapping("/sendToUser/{userId}/{message}")
    public void sendToUser(@PathVariable("userId") String userId, @PathVariable("message")String message){
        System.out.println(userId+message);
        //System.out.println(MyWebSocket.routeTable.get(userId));
        try {

            ((Session)MyWebSocket.routeTable.get(userId)).getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
