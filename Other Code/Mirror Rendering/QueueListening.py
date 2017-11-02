import requests
from bs4 import BeautifulSoup

# GET the JSON using BS4 WIP
url = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/getmessage?count=1"
lm_json = requests.get(url).json()
soup = BeautifulSoup(lm_json["Contents"],"html.parser")

print(soup)