from Scraper import Scraper
from RedisListener import RedisListener
import time

def start_bot(scraper: Scraper):
    scraper.OpenPage('https://chatgpt.com/')
    scraper.LoadCookies()
    scraper.RefreshPage()

# TODO: WHAT ABOUT \n AND ALSO NEEDS TO SEND IT
def send_prompt(scraper: Scraper, prompt: str):
    scraper.SendKeys("/html/body/div[1]/div/div[1]/div[2]/main/div/div/div[2]/div[1]/div/div/div[2]/form/div[1]/div/div[1]/div[1]/div[2]/div/div/div/div/div/p", prompt)
    return

if __name__ == '__main__':
    scraper = Scraper()
    start_bot(scraper)

    RedisListener(scraper=scraper, send_prompt=send_prompt)

    time.sleep(600)
