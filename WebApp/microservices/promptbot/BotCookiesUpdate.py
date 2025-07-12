import sys
sys.path.append('../')
from utils.Scraper import Scraper

if __name__ == '__main__':
    scraper = Scraper()
    scraper.OpenPage('https://chatgpt.com/')
    input("Have logged in?")
    scraper.SaveCookies()
    scraper.driver.quit()