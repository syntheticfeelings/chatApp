package com.syntheticfeelings.chatApp.chat;

import com.syntheticfeelings.chatApp.chat.controller.ChatController;
import com.syntheticfeelings.chatApp.chat.model.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
        ChatController.messages.add(new Message("VOVA", "sadasdadasd"));
        ChatController.messages.add(new Message("22OVA", "sadasdadasd"));
        ChatController.messages.add(new Message("1111VOVA", "sadasdadasd"));
    }
}
