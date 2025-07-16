package com.closedai.closedai.chatsystem.response;

import org.springframework.stereotype.Component;

@Component
public class HandleResponse {
    
    public void process(String message) {
        System.out.println("Processing response: " + message);
    
        //saveToDatabase(message);
    }

    //private void saveToDatabase(String message) {    
    //}

}
