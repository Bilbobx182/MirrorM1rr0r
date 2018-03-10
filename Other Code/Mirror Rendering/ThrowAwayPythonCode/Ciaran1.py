import json

import requests
from pymongo import MongoClient

base = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/"
queue = "https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo"
client = MongoClient()
db = client.SMWS
collection = db.SMWSCollection
# Have an itemLocations array that references the location
# Default it to 1 just to be safe. I just arbitrarily decided this, no greater reason.

# def addContentsToDB(contents):
#     client = MongoClient()
#
#     db = client.SMWS
#     collection = db.SMWSCollection
#     collection.insert_one(contents)
#
#
# def performRequest(count):
#     countModifier = "&count="
#     modifier = "getfifomessage?queueurl="
#     result = requests.get(base + modifier + queue + countModifier + str(count)).json()
#     messageCountLoop = 0
#
#     while (messageCountLoop < count):
#         messageKey = 'Message ' + str(messageCountLoop)
#         if (messageKey in result):
#             contents = json.loads(result[messageKey]['Contents'])
#             print(contents)
#             addContentsToDB(contents)
#         messageCountLoop += 1
#
#
# performRequest(4)

widgetsMongoObjectIdentifiers = [
    [" ", " ", " "],
    [" ", " ", " "],
    [" ", " ", " "]]



'''
Goes over all doccuments in the Database. Returns IDS for each one. 
We know that object 0 is going to be 0,0 until object 8 is 2,2 in location.
Thus we can then update when we get new widgets by getting the ID then updating.
'''
y = 0
x = 0

cursor = collection.find({})
for document in cursor:
    print(document['messagePayload'])
    widgetsMongoObjectIdentifiers[y][x] = document['_id']
    x += 1
    if (x == 3):
        x = 0
        y += 1
        if (y == 3):
            y = 0

print(widgetsMongoObjectIdentifiers)

for a in (0, 1, 2):
    for b in (0, 1, 2):
        collection.update_one({
            '_id': widgetsMongoObjectIdentifiers[a][b]
        }, {
            '$set': {
                'messagePayload': "TESTING " + str(a) + str(b)
            }
        }, upsert=False)

cursor = collection.find({})
for document in cursor:
    print(document['messagePayload'])

# TODO Take the above. Implement it to what is already had with widgets.