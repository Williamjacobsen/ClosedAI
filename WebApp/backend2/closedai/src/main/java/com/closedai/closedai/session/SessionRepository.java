package com.closedai.closedai.session;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<SessionEntity, Long> { // <SessionEntity, Long> = <JPA Entity, Primary Key>
    SessionEntity findBySessionId(String sessionId);
}
