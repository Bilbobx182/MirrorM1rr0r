import json

import requests
from kivy.app import App
from kivy.clock import Clock
from kivy.uix.gridlayout import GridLayout
from kivy.uix.image import AsyncImage
from kivy.uix.label import Label
from pymongo import MongoClient
from bson.objectid import ObjectId

client = MongoClient()

db = client.SMWS
collection = db.SMWSCollection


widgetsMongoObjectIdentifiers = [
    [" ", " ", " "],
    [" ", " ", " "],
    [" ", " ", " "]]

messageCount = 1
base = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/getfifomessage"
queue = "?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo"
count = "&count=" + str(messageCount)


def getWidgetPayloadFrom(y, x):
    widgetText = collection.find_one(widgetsMongoObjectIdentifiers[y][x])
    return (widgetText['messagePayload'])


def updateWidget(jsonContents):
    yLocation = 1
    xLocation = 1

    if ('location' in jsonContents):
        yLocation = int(jsonContents['location'].split(",")[0])
        xLocation = int(jsonContents['location'].split(",")[1])

    collection.update_one({
        '_id': widgetsMongoObjectIdentifiers[yLocation][xLocation]
    }, {
        '$set': {
            'messagePayload': jsonContents['messagePayload']
        }
    }, upsert=False)

    print(jsonContents)


def getAndSetMongoWidgetObjectIdentifiers():
    y = 0
    x = 0
    global widgetsMongoObjectIdentifiers

    cursor = collection.find({})
    for document in cursor:
        widgetsMongoObjectIdentifiers[y][x] = document['_id']
        x += 1
        if (x == 3):
            x = 0
            y += 1
            if (y == 3):
                y = 0


def performRequest():
    result = requests.get(base + queue + count).json()

    messageKey = 'Message 0'
    if (messageKey in result):
        updateWidget(json.loads(result[messageKey]['Contents']))

def setup():
    getAndSetMongoWidgetObjectIdentifiers()


class MirrorApplication(App):
    def build(self):
        setup()

        gridLayout = GridLayout(cols=3, rows=3)
        updateInterval = 5

        self.create_button(gridLayout, "MIRRROR IS LOADING")
        Clock.schedule_interval(lambda a: self.update(gridLayout), updateInterval)
        return gridLayout

    def update(self, gridLayout):
        gridLayout.clear_widgets()

        performRequest()

        for y in (0, 1, 2):
            for x in (0, 1, 2):
                self.create_button(gridLayout, getWidgetPayloadFrom(y, x))

    # Need to mark the area as empty so any new item that comes in can over-ride what was previosuly there
    # This is done so that items within the same loop can't mess each other up but new requests are fine to over-write


    def create_button(self, obj, widgetToRender):
        if 'http' in widgetToRender:
            obj.add_widget(AsyncImage(source=widgetToRender))
        else:
            label = Label(text=widgetToRender)
            label.font_size = '25dp'
            obj.add_widget(label)


# Window.fullscreen = 'auto'
MirrorApplication().run()
