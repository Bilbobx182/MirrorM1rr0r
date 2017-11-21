import requests
import time

contents = "https://i.imgur.com/f7wDYxs.jpg"
base = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/sendfifomessage"
queue = "?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo"
message = "&message=" + contents
# location = "&location=" + str(x) + "," + str(y)

result = requests.get(base + queue + message)
print(result)
print("done")
