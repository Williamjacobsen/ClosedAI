package com.closedai.closedai.prompt;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromptRepository extends CrudRepository<Prompt, String> {
}
