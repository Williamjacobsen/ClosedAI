package com.closedai.closedai.session;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

/**
 * Returns the current HTTP-session id.
 * If the client has no session cookie yet, Spring creates one automatically.
 */
@RestController
public class SessionController {

    private final SessionRepository sessionRepository;

    public SessionController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
    
    @GetMapping("/session")
    public SessionResponseDTO getOrCreateSession(HttpSession httpSession) {

        String sid = httpSession.getId();

        // --- Database ---
        // Check if session exists in database,
        // if not, save session

        // Get session
        SessionEntity entity = sessionRepository.findBySessionId(sid);
        
        // Does session exist
        if (entity == null) {
            
            // If session does not exist, then create a new session with our sid
            entity = new SessionEntity(sid);

            // Save session to database
            sessionRepository.save(entity);

        }

        // Log => SessionEntity{ id = 2, sessionId = 27D876A1829CA871D47E155E49945448, createdAt = 2025-07-13T18:11:17.322112900 }
        System.out.println(entity.toString());

        // --- Format Return & Logging ---
        SessionResponseDTO sessionResponseDTO = new SessionResponseDTO(entity.getSessionId());
        // sessionResponseDTO => {"sessionId":"4CEF7FAB6761D0177D12741CCD3DC6A8"}

        System.out.println(sessionResponseDTO.toString());
        // Log => SessionResponseDTO{ sessionId = 4CEF7FAB6761D0177D12741CCD3DC6A8 }

        return sessionResponseDTO;
        // Response => {"sessionId":"4CEF7FAB6761D0177D12741CCD3DC6A8"}
    
    }
    
}
