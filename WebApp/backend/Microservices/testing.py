from utils.RedisManager import RedisManager
import time
import threading

client = RedisManager()

def pub_prompt():
    client.redis_publisher("prompt_channel", {"key": "testid", "value": "testprompt"})
    time.sleep(10)

def sub_prompt():
    client.redis_subscriber("prompt_channel")

def sub_response():
    client.redis_subscriber("response_channel")

if __name__ == '__main__':
    threading.Thread(target=sub_prompt, daemon=True).start()
    threading.Thread(target=sub_response, daemon=True).start()

    while True:
        input("Send:")
        pub_prompt()
