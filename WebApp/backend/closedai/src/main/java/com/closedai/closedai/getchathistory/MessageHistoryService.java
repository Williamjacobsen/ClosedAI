package com.closedai.closedai.getchathistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageHistoryService {

    private final MessageHistoryRepository repo;

    public MessageHistoryService(MessageHistoryRepository repo) {
        this.repo = repo;
    }

    public Page<MessageHistory> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }
}
