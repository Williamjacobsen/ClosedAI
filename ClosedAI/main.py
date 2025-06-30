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

def wait_for_response(scraper, xpath, timeout=300, check_interval=2):
    start_time = time.time()
    last_text = ""
    stable_count = 0

    while time.time() - start_time < timeout:
        current_text = scraper.GetText(xpath)

        if current_text == "":
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
    # /html/body/div[1]/div/div[1]/div[2]/main/div/div/div[1]/div/div/div[2]/article[last()]
    # /html/body/div[1]/div/div[1]/div[2]/main/div/div/div[1]/div/div/div[2]/article[last()]/div/div/div/div/div[1]

    response = wait_for_response(scraper, "/html/body/div[1]/div/div[1]/div[2]/main/div/div/div[1]/div/div/div[2]/article[last()]/div/div/div/div/div[1]")

    formatted_response = ""

    # /html/body/div[1]/div/div[1]/div[2]/main/div/div/div[1]/div/div/div[2]/article[last()]/div/div/div/div/div[1]/div/div/div
    response_elements = scraper.GetChildren("/html/body/div[1]/div/div[1]/div[2]/main/div/div/div[1]/div/div/div[2]/article[last()]/div/div/div/div/div[1]/div/div/div")

    # /html/body/div[1]/div/div[1]/div[2]/main/div/div/div[1]/div/div/div[2]/article[last()]/div/div/div/div/div[1]/div/div/div/p[1] # text
    # /html/body/div[1]/div/div[1]/div[2]/main/div/div/div[1]/div/div/div[2]/article[last()]/div/div/div/div/div[1]/div/div/div/div # table (class="_tableContainer_80l1q_1")
    # /html/body/div[1]/div/div[1]/div[2]/main/div/div/div[1]/div/div/div[2]/article[last()]/div/div/div/div/div[1]/div/div/div/p[2] # text

    for element in response_elements:
        print(element.tag_name) # p for text and div for tables
        print(element.get_attribute("class")) # _tableContainer_80l1q_1 for tables

        tag = element.tag_name
        _class = element.get_attribute("class")

        if tag == "p": # text
            formatted_response += scraper.GetTextFromElement(element)
        
        elif tag == "div" and _class == "_tableContainer_80l1q_1": # table

            # Structure:
            # div: _tableContainer_80l1q_1
            #   div
            #       table
            #           thead
            #               tr - (first) row of keys
            #                   th - each column / value of the row
            #                   ...
            #                   th
            #           tbody
            #               tr - each row (of values)
            #               ...
            #               tr
            #                   td - each value of the row
            #                   ...
            #                   td

            thead = scraper.FindFirstTagFromElement(element, "thead")
            tr = scraper.GetChildByIndex(thead, 0)
            th_list = scraper.GetChildrenFromElement(tr)
            for th in th_list:
                print(scraper.GetTextFromElement(th)) # TODO: handle this
            # TODO

            formatted_response += scraper.GetTextFromElement(element)
        
        else:
            formatted_response += scraper.GetTextFromElement(element)

    return response

if __name__ == '__main__':
    scraper = Scraper()
    start_bot(scraper)

    RedisListener(scraper=scraper, send_prompt=send_prompt, get_response=get_response)

    time.sleep(600)
