import sys
sys.path.append('../')

from utils.Scraper import Scraper
import time

def check_log_in_success(): # TODO
    # /html/body/div[4]/div/div/div/div/div/p[1] = "Welcome back"
    pass

def start_bot(scraper: Scraper):
    scraper.OpenPage('https://chatgpt.com/')
    scraper.LoadCookies()
    scraper.RefreshPage()

def send_prompt(scraper: Scraper, prompt: str):
    print("Prompt:")
    print(prompt)
    
    # TODO: XPath may change, add a function in Scraper.py for finding an element by text.
    # From:
    #        /html/body/div[1]/div/div[1]/div[2]/main/div/div/div[2]/div[1]/div/div/div[2]/form/div[1]/div/div[1]/div[1]/div[2]/div/div/div/div/div/p
    # To:
    #        /html/body/div[1]/div/div[1]/div[2]/main/div/div/div[2]/div[1]/div/div/div[2]/form/div[2]/div/div[1]/div[1]/div[2]/div/div/div/div/div/p
    
    xpath = "/html/body/div[1]/div/div[1]/div[2]/main/div/div/div[2]/div[1]/div/div/div[2]/form/div[2]/div/div[1]/div[1]/div[2]/div/div/div/div/div/p"

    # TODO:
    #Received on prompt_channel:
    #Key    : None
    #Value  : None
    #[RedisSubscriber] Callback for ID=prompt_channel: None
    #Prompt:
    #None
    #Error subscribing to prompt_channel: 'NoneType' object has no attribute 'splitlines'
    #Exception ignored in: <function Chrome.__del__ at 0x000001B32A2A6480>
    #Traceback (most recent call last):
    #  File "C:\Users\villi\AppData\Roaming\Python\Python313\site-packages\undetected_chromedriver\__init__.py", line 843, in __del__
    #  File "C:\Users\villi\AppData\Roaming\Python\Python313\site-packages\undetected_chromedriver\__init__.py", line 798, in quit
    #OSError: [WinError 6] The handle is invalid

    lines = prompt.splitlines()
    for idx, line in enumerate(lines):
        if line:
            scraper.SendKeys(xpath, line)
        if idx < len(lines) - 1:
            scraper.SendKeys(xpath, scraper.keys.SHIFT, scraper.keys.ENTER)
    
    scraper.SendKeys(xpath, scraper.keys.ENTER)
    return

def wait_for_response(scraper, xpath, timeout=300, check_interval=0.5):
    start_time = time.time()
    last_text = ""
    stable_count = 0

    connection_established = False

    while time.time() - start_time < timeout:
        current_text = scraper.GetText(xpath)

        if current_text.strip() == "":
            continue

        if not connection_established:
            connection_established = True

        if current_text == last_text:
            stable_count += 1
        else:
            stable_count = 0
            last_text = current_text

        if stable_count >= 6:
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

def run():
    scraper = Scraper()
    start_bot(scraper)
    return scraper

if __name__ == '__main__':
    run()
