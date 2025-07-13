package com.closedai.closedai.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface SessionRepository extends JpaRepository<SessionEntity, Long> { // <SessionEntity, Long> = <JPA Entity, Primary Key>
    
    SessionEntity findBySessionId(String sessionId);

    @Modifying
    @Transactional
    @Query("UPDATE SessionEntity s SET s.lastUsed = CURRENT_TIMESTAMP WHERE s.sessionId = :sessionId")
    void updateLastActive(@Param("sessionId") String sessionId);

}
