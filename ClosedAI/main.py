from Scraper import Scraper
import time


if __name__ == '__main__':
    scraper = Scraper()
    
    scraper.OpenPage('https://chatgpt.com/')
    
    scraper.LoadCookies()

    scraper.RefreshPage()

    time.sleep(600)


