package com.closedai.closedai.prompt;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class PromptService {

    private final PromptRepository repository;

    public PromptService(PromptRepository repository) {
        this.repository = repository;
    }

    public Prompt savePrompt(Prompt prompt) {
        System.out.println("Saving prompt: " + prompt.getId() + " - " + prompt.getContent());
        return repository.save(prompt);
    }

    public Optional<Prompt> getPrompt(String id) {
        return repository.findById(id);
    }

    public Iterable<Prompt> getAllPrompts() {
        return repository.findAll();
    }

    public void deletePrompt(String id) {
        repository.deleteById(id);
    }
}
