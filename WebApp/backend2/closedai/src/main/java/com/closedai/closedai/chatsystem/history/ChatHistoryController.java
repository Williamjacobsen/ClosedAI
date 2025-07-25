package com.closedai.closedai.chatsystem.history;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatHistoryController {
    
    @GetMapping("/get-chat-history")
    public String test(@CookieValue(value = "SESSION_ID", required=true) String cookieSessionId) {
        return "test";
    }
    
}
