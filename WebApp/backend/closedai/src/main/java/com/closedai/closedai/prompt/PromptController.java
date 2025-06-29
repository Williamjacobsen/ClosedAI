package com.closedai.closedai.prompt;

import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.closedai.closedai.redisqueue.RedisQueueService;

@RestController
@RequestMapping("/prompt")
public class PromptController {

    private final PromptService service;
    private final RedisQueueService redisQueueService;

    public PromptController(PromptService service, RedisQueueService redisQueueService) {
        this.service = service;
        this.redisQueueService = redisQueueService;
    }

    @PostMapping
    public Prompt createPrompt(@RequestBody Prompt prompt) {
        return service.savePrompt(prompt);
    }

    @GetMapping("/{id}")
    public Optional<Prompt> getPrompt(@PathVariable String id) {
        return service.getPrompt(id);
    }

    @GetMapping
    public Iterable<Prompt> getAllPrompts() {
        return service.getAllPrompts();
    }

    @DeleteMapping("/{id}")
    public void deletePrompt(@PathVariable String id) {
        service.deletePrompt(id);
    }

    @PostMapping("/queue")
    public String queuePrompt(@RequestBody Prompt prompt) {
        redisQueueService.pushPrompt(prompt.getId(), prompt.getContent());
        return "Prompt queued";
    }

    @GetMapping("/response/{id}")
    public String getPromptResponse(@PathVariable String id) {
        return redisQueueService.getResponse(id);
    }
}
