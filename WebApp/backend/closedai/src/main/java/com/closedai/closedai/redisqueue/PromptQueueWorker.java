package com.closedai.closedai.redisqueue;

/*
 * PromptQueueWorker
 * -----------------
 * A self‑contained background bean that consumes a Redis list as a FIFO work
 * queue.  The worker blocks with BLPOP semantics (via Spring Data's
 * `leftPop(.., Duration)`), processes each JSON payload, and retries
 * gracefully when Redis is unreachable.
 *
 * • Spring Boot 3.5.x  ·  Spring Data Redis 3.x  ·  JDK 21+ (works on 17+)
 */

/* ── Java ─────────────────────────────────────────────────────────────── */
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

@Component
public class PromptQueueWorker implements InitializingBean {

    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ constants ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    /** Redis list key that producers push to and this worker pops from. */
    private static final String QUEUE_KEY = "prompt-queue";

    /** Class‑specific logger (no Lombok). */
    private static final Logger log = LoggerFactory.getLogger(PromptQueueWorker.class);

    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ dependencies ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    private final StringRedisTemplate redis;           // injected by Spring
    private final ExecutorService pool =               // one lightweight worker thread
            Executors.newSingleThreadExecutor();

    private volatile boolean running = true;           // stop‑signal for the loop

    public PromptQueueWorker(StringRedisTemplate redis) {
        this.redis = redis;
    }

    /* =============================================================================
       Lifecycle: start the background loop once Spring has wired the bean
       ============================================================================= */
    @Override
    public void afterPropertiesSet() {
        pool.submit(this::runLoop);                    // own thread owns the loop
    }

    /* ----------------------------------------------------------------------------- */
    private void runLoop() {
        while (running) {
            try {
                // Blocks up to 1s -> BLPOP key 1
                String msg = redis.opsForList().leftPop(QUEUE_KEY, Duration.ofSeconds(1));
                if (msg != null) handle(msg);
            } catch (Exception ex) {
                log.error("Queue polling failed - retrying in 1s", ex);
                backOffOneSecond();
            }
        }
    }

    /* ----------------------------------------------------------------------------- */
    private void handle(String json) {
        System.out.println("Dequeued -> " + json);
        log.info("Dequeued -> {}", json);
        // TODO: parse JSON and invoke your business logic
    }

    /* ----------------------------------------------------------------------------- */
    private void backOffOneSecond() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    /* =============================================================================
       Graceful shutdown
       ============================================================================= */
    @PreDestroy
    public void stop() {
        running = false;               // lets the loop exit
        pool.shutdownNow();            // interrupt blocking operations
        try { pool.awaitTermination(5, TimeUnit.SECONDS); }
        catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
    }
}
