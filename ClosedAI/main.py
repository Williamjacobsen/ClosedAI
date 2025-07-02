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

def wait_for_response(scraper, xpath, timeout=300, check_interval=1):
    start_time = time.time()
    last_text = ""
    stable_count = 0

    while time.time() - start_time < timeout:
        current_text = scraper.GetText(xpath)

        if current_text.strip() == "":
            continue

        if current_text == last_text:
            stable_count += 1
        else:
            stable_count = 0
            last_text = current_text

        if stable_count >= 2:
            break

        time.sleep(check_interval)

    return last_text

def get_response(scraper: Scraper):
    response_xpath = "/html/body/div[1]/div/div[1]/div[2]/main/div/div/div[1]/div/div/div[2]/article[last()]/div/div/div/div/div[1]"

    wait_for_response_to_finish = wait_for_response(scraper, response_xpath)

    if wait_for_response_to_finish == "":
        raise ValueError("No response value") # TODO: Shouldn't crash app because no response

    element = scraper.GetElement(response_xpath)

    responseOuterHTML = scraper.GetOuterHTMLFromElement(element)

    print(responseOuterHTML)

    return responseOuterHTML

if __name__ == '__main__':
    scraper = Scraper()
    start_bot(scraper)

    RedisListener(scraper=scraper, send_prompt=send_prompt, get_response=get_response)

    time.sleep(600)
