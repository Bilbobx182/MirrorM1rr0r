import requests

# Keeping this as I will need a dedicated class later for queue listening
url = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/getmessage?count=1"
lm_json = requests.get(url).json()
basicURL = lm_json['0']['Contents']

print(basicURL)