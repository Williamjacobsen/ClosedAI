from Scraper import Scraper
from RedisListener import RedisListener
import time

def start_bot(scraper: Scraper):
    scraper.OpenPage('https://chatgpt.com/')
    scraper.LoadCookies()
    scraper.RefreshPage()

def send_prompt(scraper: Scraper, prompt: str):
    print("Prompt:")
    print(prompt)
    
    xpath = "/html/body/div[1]/div/div[1]/div[2]/main/div/div/div[2]/div[1]/div/div/div[2]/form/div[1]/div/div[1]/div[1]/div[2]/div/div/div/div/div/p"

    lines = prompt.splitlines()
    for idx, line in enumerate(lines):
        if line:
            scraper.SendKeys(xpath, line)
        if idx < len(lines) - 1:
            scraper.SendKeys(xpath, scraper.keys.SHIFT, scraper.keys.ENTER)
    
    scraper.SendKeys(xpath, scraper.keys.ENTER)
    return

def get_all_responses(scraper: Scraper):
    # Skip every two, start at 2
    # /html/body/div[1]/div/div[1]/div[2]/main/div/div/div[1]/div/div/div[2]/article[1]
    # /html/body/div[1]/div/div[1]/div[2]/main/div/div/div[1]/div/div/div[2]/article[6]

    return

def get_response(scraper: Scraper):
    # /html/body/div[1]/div/div[1]/div[2]/main/div/div/div[1]/div/div/div[2]/article[last()]

    

    return

if __name__ == '__main__':
    scraper = Scraper()
    start_bot(scraper)

    RedisListener(scraper=scraper, send_prompt=send_prompt, get_response=get_response)

    time.sleep(600)
