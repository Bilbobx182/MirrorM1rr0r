import requests

rehydrateCount = 0
while rehydrateCount < 100:
    # if rehydrateCount % 2 == 0:
    #     url = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/sendmessagetoqueue?queue=MirrorM1rr0r&message=https://i.imgur.com/NwWb1v5.jpg"
    #     lm_json = requests.get(url)
    # else:
    #     url = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/sendmessagetoqueue?queue=MirrorM1rr0r&message=ReHydrateYo"
    #     lm_json = requests.get(url)
    # rehydrateCount+=1

    url = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/sendmessagetoqueue?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/hellociaran&message=ciaran"
    lm_json = requests.get(url)