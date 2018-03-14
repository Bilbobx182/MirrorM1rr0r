from pymongo import MongoClient

client = MongoClient()

db = client.SMWS
collection = db.SMWSCollection

queryResult = db.SMWSCollection.find_one()
print(queryResult)

loopCount = 0
x = 0
y = 0

while loopCount < 9:

    collection.insert_one(
        {
            'messagePayload': str(loopCount),
            'location': str(y) + "," + str(x),
            "fontColour": "ffffff",
            "fontSize": 25,

            "dynamicIdentifier": {
                "command": "",
                "extraMessage": "",
                "lat": "",
                "long": ""
            }
        }
    )

    loopCount += 1
    x += 1
    if (x == 3):
        x = 0
        y += 1
        if (y == 3):
            y = 0
