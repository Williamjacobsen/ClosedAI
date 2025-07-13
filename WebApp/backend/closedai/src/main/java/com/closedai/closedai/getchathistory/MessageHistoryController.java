package com.closedai.closedai.getchathistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageHistoryController {

    private final MessageHistoryService service;

    public MessageHistoryController(MessageHistoryService service) {
        this.service = service;
    }

    /**
     * Examples
     * ─────────────────────────────────────────────────────────────
     * GET /api/messages                        --> newest 10 messages
     * GET /api/messages?page=1                 --> next 10 older
     * GET /api/messages?page=2&size=20         --> 3rd oldest slice, 20 rows
     *
     * You can still override the sort:
     * GET /api/messages?sort=channel,asc       --> ignores the default
     * 
     * GET /api/messages?sort=receivedAt,desc   --> most recent 10 messages
     */
    @GetMapping
    public Page<MessageHistory> list(
            @PageableDefault(size = 10)                   // default page size
            @SortDefault(sort = "receivedAt",
                         direction = Direction.DESC)      // default ordering
            Pageable pageable) {

        /** This should be done at client side for less 'company' computing expenses,
         * But i want to practice my java
         */
        
        // 1. fetch the slice
        Page<MessageHistory> page = service.findAll(pageable);

        // 2. reverse the rows inside that slice
        List<MessageHistory> reversed = new ArrayList<>(page.getContent());
        Collections.reverse(reversed);

        // 3. return a Page with the same metadata but reversed content
        return new PageImpl<>(reversed, pageable, page.getTotalElements());
    }
}
