package com.closedai.closedai.prompt;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Prompt")
public class Prompt implements Serializable {

    @Id
    private String id;
    private String content;

    // Constructors
    public Prompt() {}

    public Prompt(String id, String content) {
        this.id = id;
        this.content = content;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
