import redis
import json

# Connect to Redis server
redis_client = redis.Redis(host='localhost', port=6379, decode_responses=True)

# Subscribe to the Redis channel
pubsub = redis_client.pubsub()
pubsub.subscribe('prompt_channel')
pubsub.subscribe('response_channel')

print("Listening on 'prompt_channel'...")

# Start listening for messages
for message in pubsub.listen():
    if message['type'] == 'message':
        try:
            data = json.loads(message['data'])
            prompt_id = data.get('id')
            prompt_text = data.get('prompt')

            print(f"Received Prompt:")
            print(f"   ID    : {prompt_id}")
            print(f"   Prompt: {prompt_text}")

            # You can process the prompt here...
            # For example:
            # result = your_ai_function(prompt_text)
            # respond_back(prompt_id, result)

        except json.JSONDecodeError:
            print("Received non-JSON message:", message['data'])
