
# TODO: Error loading cookies: [Errno 2] No such file or directory: 'cookies.pkl'


import sys
sys.path.append('../')

from utils.RedisManager import RedisManager
import promptbot

redis_client = RedisManager()

def ReturnResponse(prompt_id, response):
    redis_client.redis_publisher("response_channel", {
        "key": prompt_id,
        "value": response
    })

def GetResponse(prompt_id):
    response = promptbot.get_response(scraper=scraper) # Blocks thread until response received
    print(f"[RedisSubscriber] Response for ID {prompt_id}:\n{response}")
    ReturnResponse(prompt_id, response)

def RedisSubscriber(scraper):

    def handle_message(channel, prompt_id, prompt_text):
        print(f"[RedisSubscriber] Callback for ID={prompt_id}: {prompt_text}")
        promptbot.send_prompt(scraper=scraper, prompt=prompt_text)
        GetResponse(prompt_id)

    redis_client.redis_subscriber(channels=["prompt_channel"], callback=handle_message)

if __name__ == '__main__':
    scraper = promptbot.run()

    RedisSubscriber(scraper=scraper)
