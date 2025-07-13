package com.closedai.closedai.getchathistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageHistoryRepository extends JpaRepository<MessageHistory, Long> {
}
