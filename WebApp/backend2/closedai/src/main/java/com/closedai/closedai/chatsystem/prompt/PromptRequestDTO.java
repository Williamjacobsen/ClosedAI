package com.closedai.closedai.chatsystem.prompt;

public class PromptRequestDTO {
    
    private String prompt;
    private String chatSessionName;

    public String getPrompt() { return this.prompt; }
    public void setPrompt(String prompt)   { this.prompt = prompt; }

    public String getChatSessionName() { return this.chatSessionName; }
    public void setChatSessionName(String chatSessionName) { this.chatSessionName = chatSessionName; }

    @Override
    public String toString() {
        return String.format("PromptRequestDTO{ prompt = %s, chatSessionName = %s }", prompt, chatSessionName);
    }

}
