import requests
import time

rehydrateCount = 0
while rehydrateCount < 40:

    x = 0
    y = 0
    contents = "Message: " + str(rehydrateCount)
    base = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/sendfifomessage"
    queue = "?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo"
    message = "&message=" + contents
    location = "&location=" + str(x) + "," + str(y)

    result = requests.get(base + queue + message + location)
    rehydrateCount += 1
    print(rehydrateCount)

    if (x == 2):
        x = 0
    else:
        x += 1

    if (y == 2):
        y = 0
    else:
        y += 1

print("done")
