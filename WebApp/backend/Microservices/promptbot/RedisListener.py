def RedisListener(scraper, send_prompt, get_response):
    import redis
    import json

    r = redis.Redis(host='localhost', port=6379, decode_responses=True)

    print("Listening for prompts...")

    while True:
        # Further development: r.brpop(["prompt_queue:user1", "prompt_queue:user2", ...])
        _, data = r.brpop("prompt_queue")
        prompt = json.loads(data)

        prompt_id = prompt["id"]
        content = prompt["content"]

        print(f"Received prompt: {content}")

        send_prompt(scraper=scraper, prompt=content)

        response = get_response(scraper=scraper)

        # Store response
        r.set(f"response:{prompt_id}", response)
        print(f"Response stored at response:{prompt_id}")

        r.publish(
            "prompt_response_channel",
            json.dumps({"id": prompt_id})
        )

if __name__ == '__main__':
    RedisListener(lambda: "test1", lambda: "test2")