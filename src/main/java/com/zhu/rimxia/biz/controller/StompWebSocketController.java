package com.zhu.rimxia.biz.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wsTemplate")
public class StompWebSocketController {

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/user/{userId}/{message}")
    public void sendToUser(@PathVariable("userId") Long userId, @PathVariable("message") String message){
        messagingTemplate.convertAndSend("/topic/"+userId,message);
    }
}
