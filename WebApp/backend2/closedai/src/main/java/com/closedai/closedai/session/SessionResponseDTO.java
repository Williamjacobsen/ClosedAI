package com.closedai.closedai.session;

/** JSON response payload: { "sessionId": "abc123" } */
public class SessionResponseDTO {

    // --------- Fields ---------
    private String sessionId;

    // --------- Constructors ---------
    public SessionResponseDTO() { // DTO = Data Transfer Object
        // Public no-arg ctor required by Jackson for JSON → POJO deserialization
    }

    public SessionResponseDTO(String sessionId) {
        this.sessionId = sessionId;
    }

    // --------- Getters & Setters ---------
    public String getSessionId() {
        return this.sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    // --------- Custom Functions ---------
    @Override
    public String toString() {
        return String.format("SessionResponseDTO{ sessionId = %s }", sessionId);
    }

}
