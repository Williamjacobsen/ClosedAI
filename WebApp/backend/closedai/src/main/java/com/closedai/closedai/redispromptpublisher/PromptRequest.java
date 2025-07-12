package com.closedai.closedai.redispromptpublisher;

// Plain Java class (POJO) used to represent the JSON payload sent by the client

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PromptRequest {
    
    @JsonAlias({"id", "key"})    // accept either name on input
    @JsonProperty("key")         // always output as "key"
    private String id;

    @JsonAlias({"prompt", "value"})
    @JsonProperty("value")
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
