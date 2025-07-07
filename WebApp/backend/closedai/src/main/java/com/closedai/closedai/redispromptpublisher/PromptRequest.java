package com.closedai.closedai.redispromptpublisher;

// Plain Java class (POJO) used to represent the JSON payload sent by the client
public class PromptRequest {
    private String id;
    private String prompt;

    // Required by Spring to deserialize incoming JSON into this object
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
