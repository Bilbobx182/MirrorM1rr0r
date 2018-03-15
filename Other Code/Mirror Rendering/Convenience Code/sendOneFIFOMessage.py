import requests
import time
import os

# contents = str(os.urandom(5))
contents = "oh hi mark"
base = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/sendfifomessage"
queue = "?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/1143c19ff83da8d2de3fa74df9fbcbcf.fifo"
message = "&message=" + "^/^temp"
location = "&location=" + "0,1"
lat = "&lat=53.35"
long = "&long=-6.35"
# location = "&location=" + str(x) + "," + str(y)

result = requests.get(base + queue + message + location + lat + long)
print(result)
print("done")
