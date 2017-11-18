import requests
import time

rehydrateCount = 0
while rehydrateCount < 40:

    contents = "Message: " + str(rehydrateCount)
    url = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/sendfifomessage?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo&message=" + contents
    result = requests.get(url)
    rehydrateCount+=1
    print(rehydrateCount)

print("done")