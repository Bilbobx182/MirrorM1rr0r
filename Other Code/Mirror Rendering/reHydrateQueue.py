import requests
import time

rehydrateCount = 0
while rehydrateCount < 10:
    # if rehydrateCount % 2 == 0:
    #     url = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/sendmessagetoqueue?queue=MirrorM1rr0r&message=https://i.imgur.com/NwWb1v5.jpg"
    #     lm_json = requests.get(url)
    # else:
    #     url = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/sendmessagetoqueue?queue=MirrorM1rr0r&message=ReHydrateYo"
    #     lm_json = requests.get(url)
    # rehydrateCount+=1

    if(rehydrateCount % 2 == 0):
        contents = 'https://image.flaticon.com/icons/png/128/168/168570.png'
    else:
        contents = "Message: " + str(rehydrateCount)

    url = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/sendmessagetoqueue?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/normalQueue&message=" + contents
    lm_json = requests.get(url)
    print(lm_json)
    time.sleep(1)
    rehydrateCount+=1
    print(rehydrateCount)

print("done")