import requests
import time
from random import randint

rehydrateCount = 0
x = 0
y = 0

dogImages = ["https://i.imgur.com/6hcxDaE.jpg", "https://i.imgur.com/f7wDYxs.jpg", "https://i.imgur.com/aV0PkUu.jpg",
             "https://i.imgur.com/Vm3EGQf.png"]

while rehydrateCount < 18:
    contents = "Message: " + str(rehydrateCount)
    base = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/sendfifomessage"
    queue = "?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo"
    if(rehydrateCount % 3== 0):
        message = dogImages[randint(0, 3)]
    else :
        message = "&message=" + contents
    location = "&location=" + str(x) + "," + str(y)

    result = requests.get(base + queue + message + location)
    print("Y " + str(y) + " : X " + str(x))

    x += 1
    if (x == 3):
        x = 0
        y += 1
        if (y == 3):
            y = 0

    rehydrateCount += 1

print("done")
