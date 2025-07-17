from utils.RedisManager import RedisManager
import time
import threading

client = RedisManager()

def pub_prompt():
    client.redis_publisher("prompt_channel", {"key": "testid", "value": "testprompt"})

def pub_response():
    client.redis_publisher(
        "response_channel", 
        {
            "sessionId": "05bd7ce1-c2ea-40fe-90dd-cc7de51ef151",
            "chatSessionName": "defualt",
            "response": "my test response"
        }
    )

def callbackFunc(channel, data):
    print(f"Message on [{channel}]:")
    print(data)
    simulate_bot_response()

def subscribers():
    client.redis_subscriber(channels=["prompt_channel", "response_channel"], callback=callbackFunc)

def simulate_bot_response():
    time.sleep(10)
    pub_response()

if __name__ == '__main__':
    threading.Thread(target=subscribers, daemon=True).start()

    while True:
        time.sleep(0.1)
        input("Send:")
        pub_response()
