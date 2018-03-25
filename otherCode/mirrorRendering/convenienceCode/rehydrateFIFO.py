import random

import requests

rehydrateCount = 0
x = 0
y = 0

# dogImages = ["https://i.imgur.com/6hcxDaE.jpg", "https://i.imgur.com/f7wDYxs.jpg", "https://i.imgur.com/aV0PkUu.jpg",
#              "https://i.imgur.com/Vm3EGQf.png"]

while rehydrateCount < 100:
    contents = "Message: " + str(rehydrateCount)
    base = "https://trbcvi749b.execute-api.eu-west-1.amazonaws.com/Prod/sendmessage"
    # queue = "?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo"
    queue = "?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/1143c19ff83da8d2de3fa74df9fbcbcf.fifo"
    # if (rehydrateCount % 3 == 0):
    #     message = "&message=" + dogImages[randint(0, 3)]
    # else:

    randomMessageType = ["Gone to Shop", "https://i.imgur.com/6hcxDaE.jpg", "^/^weather", "^/^tempature"]

    # message = "&message=" + str(os.urandom(5)) + "test"
    message = "&message=" + randomMessageType[random.randint(0, 3)]
    fontColour = "&fontcolour=" + '%02X%02X%02X' % (
        random.randint(0, 255), random.randint(0, 255), random.randint(0, 255))
    location = "&location=" + str(x) + "," + str(y)
    lat = "&lat=53.35"
    long = "&long=-6.35"

    result = requests.get(base + queue + message + fontColour + lat + long + location)
    print(base + queue + message + fontColour + lat + long + location)
    print("Y " + str(y) + " : X " + str(x))

    x += 1
    if (x == 3):
        x = 0
        y += 1
        if (y == 3):
            y = 0

    rehydrateCount += 1

print("done")
