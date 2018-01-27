import pymongo
from pymongo import MongoClient

client = MongoClient()

db = client.SMWS
collection = db.SMWSCollection

queryResult = db.SMWSCollection.find_one()

print(queryResult)