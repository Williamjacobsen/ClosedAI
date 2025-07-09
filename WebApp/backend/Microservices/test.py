from utils.RedisManager import RedisManager
import time

client = RedisManager()

while True:
    client.redis_publisher("prompt_channel", {"id": "testid", "prompt": "testprompt"})
    time.sleep(10)
