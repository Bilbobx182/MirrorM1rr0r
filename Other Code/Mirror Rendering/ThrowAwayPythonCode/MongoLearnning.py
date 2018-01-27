import pymongo
from pymongo import MongoClient

client = MongoClient()

db = client.SMWS
collection = db.SMWSCollection

queryResult = db.SMWSCollection.find_one()
print(queryResult)

# collection.insert_one({'Text': "SometextHereInsertedByPython", 'X': 1, 'Y': 1})
# I need to itterate over the results as it returns a cursor object using the find method
for item in collection.find({'X': 1}):
    print(item)
