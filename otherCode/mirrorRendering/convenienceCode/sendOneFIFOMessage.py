import random

import requests

# contents = str(os.urandom(5))
contents = "oh hi mark"
base = "https://trbcvi749b.execute-api.eu-west-1.amazonaws.com/Prod/sendmessage/"
queue = "?queueurl=https://eu-west-1.queue.amazonaws.com/186314837751/3a22e9d4.fifo"
message = "&message=" + "https://www.blink182.com/wp-content/uploads/2017/01/zo3EK9Uq.jpg"
fontColour = "&fontcolour=" + '%02X%02X%02X' % (
    random.randint(0, 255), random.randint(0, 255), random.randint(0, 255))
location = "&location=" + "0,2"
lat = "&lat=53.35"
long = "&long=-6.45"
# location = "&location=" + str(x) + "," + str(y)
url = base + queue + message + location + lat + long + fontColour
result = requests.get(url)
print(url)
print(result)
print("done")
