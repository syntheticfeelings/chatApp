package com.syntheticfeelings.chatApp.chat.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syntheticfeelings.chatApp.chat.model.Message;
import com.syntheticfeelings.chatApp.chat.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


@RestController
@RequestMapping("chat")
public class ChatController {

    private Gson gson = new GsonBuilder().create();
    public static Queue<Message> messages = new ConcurrentLinkedQueue<>();
    public static Map<Integer, User> usersOnline = new ConcurrentHashMap<>();

    /**
     * curl -X POST -i localhost:8888/chat/login -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    @CrossOrigin
    public ResponseEntity<String> login(@RequestParam("name") String name) {
        if (name.length() < 2) {
            return ResponseEntity.badRequest().body("Name is too short\n");
        }
        if (name.length() > 30) {
            return ResponseEntity.badRequest().body("Name is too long\n");
        }
        if (name.equals("I_DID_NOT_CHANGE_DEFAULT_NAME")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You did not change default name, didn't you?\n");
        }
        if (usersOnline.containsKey(name.hashCode())) {
            return ResponseEntity.badRequest().body("Already logged in\n");
        }
        usersOnline.put(name.hashCode(), new User(name));
        messages.add(new Message(name, "online"));
        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8888/chat/online
     */
    @RequestMapping(
            path = "online",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @CrossOrigin
    public ResponseEntity online() {

        return ResponseEntity.ok(gson.toJson(usersOnline));
    }

    /**
     * curl -X POST -i localhost:8888/chat/logout -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "logout",
            method = RequestMethod.DELETE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @CrossOrigin
    public ResponseEntity logout(@RequestParam("name") String name) {
        if (!usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("User is offline\n");
        }
        usersOnline.remove(name);
        messages.add(new Message(name, "logged out"));
        return ResponseEntity.ok().build();
    }


    /**
     * curl -X POST -i localhost:8888/chat/say -d "name=I_AM_STUPID&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = "say",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @CrossOrigin
    public ResponseEntity say(@RequestParam("name") String name, @RequestParam("msg") String msg) {
        if (!usersOnline.containsKey(name.hashCode())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not online\n");
        }
        if (msg.length() > 140) {
            return ResponseEntity.badRequest().body("Message is too long\n");
        }
        messages.add(new Message(name, msg));
        return ResponseEntity.ok().build();
    }


    /**
     * curl -i localhost:8888/chat/history
     */
    @RequestMapping(
            path = "history",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CrossOrigin
    public ResponseEntity history() {

        return ResponseEntity.ok().body(gson.toJson(messages));
    }
}
