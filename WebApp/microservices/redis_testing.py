from utils.RedisManager import RedisManager
import time

client = RedisManager()

def pub_prompt():
    client.redis_publisher("prompt_channel", {"key": "testid", "value": "testprompt"})

def pub_response(data):
    client.redis_publisher(
        "response_channel", 
        {
            "sessionId": data.sessionId,
            "chatSessionName": data.chatSessionName,
            "response": "my test response"
        }
    )

def callbackFunc(channel, data):
    print(f"Message on [{channel}]:")
    print(data)
    if channel == "prompt_channel":
        simulate_bot_response(data)

def subscribers():
    client.redis_subscriber(channels=["prompt_channel", "response_channel"], callback=callbackFunc)

def simulate_bot_response(data):
    time.sleep(5)
    pub_response(data)

if __name__ == '__main__':
    subscribers()
