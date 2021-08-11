package com.zyh.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WsController {
    @Autowired
    SimpMessagingTemplate messagingTemplate;

//    @MessageMapping("/ws/chat")
//    public void handleChat(Principal principal, String msg) {
//        String destUser = msg.substring(msg.lastIndexOf(";") + 1, msg.length());
//        String message = msg.substring(0, msg.lastIndexOf(";"));
//        messagingTemplate.convertAndSendToUser(destUser, "/queue/chat", new ChatResp(message, principal.getName()));
//    }
   // 消息协议很简单：发送来的消息，最后一个;
    // 后面跟的是该条消息要发送到哪个用户，这里通过字符串截取将之提取出来。
    // 响应消息包含两个字段，一个是消息内容，一个是该条消息由谁发送。
}
