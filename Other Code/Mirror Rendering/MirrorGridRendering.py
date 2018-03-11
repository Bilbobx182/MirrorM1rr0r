import json

import requests
from kivy.app import App
from kivy.clock import Clock
from kivy.uix.gridlayout import GridLayout
from kivy.uix.image import AsyncImage
from kivy.uix.label import Label
from pymongo import MongoClient
import matplotlib.colors as colors

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


def doesWidgetHaveColourAttribute(y, x):
    widgetJSON = collection.find_one(widgetsMongoObjectIdentifiers[y][x])
    if ('fontColour' in widgetJSON):
        return True
    return False


def doesWidgetHaveSizeAttribute(y, x):
    widgetJSON = collection.find_one(widgetsMongoObjectIdentifiers[y][x])
    if ('fontSize' in widgetJSON):
        return True
    return False


def getWidgetColourAttribute(y, x):
    widgetJSON = collection.find_one(widgetsMongoObjectIdentifiers[y][x])
    return widgetJSON['fontColour']


def getWidgetSizeAttribute(y, x):
    widgetJSON = collection.find_one(widgetsMongoObjectIdentifiers[y][x])
    return widgetJSON['fontSize']


def updateWidget(jsonContents):
    yLocation = 1
    xLocation = 1

    fontSize = ""
    fontColour = "ffffff"

    if ('location' in jsonContents):
        yLocation = int(jsonContents['location'].split(",")[0])
        xLocation = int(jsonContents['location'].split(",")[1])

    if ('fontColour' in jsonContents):
        fontColour = jsonContents['fontColour']

    if ('fontSize' in jsonContents):
        fontSize = jsonContents['fontSize']
    else:
        fontSize = 25

    collection.update_one({
        '_id': widgetsMongoObjectIdentifiers[yLocation][xLocation]
    }, {
        '$set': {
            'messagePayload': jsonContents['messagePayload'],
            'fontColour': fontColour,
            'fontSize': fontSize
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
        gridLayout.add_widget(Label(text="Loading now!"))

        updateInterval = 5
        Clock.schedule_interval(lambda a: self.update(gridLayout), updateInterval)
        return gridLayout

    def update(self, gridLayout):
        gridLayout.clear_widgets()

        performRequest()

        for y in (0, 1, 2):
            for x in (0, 1, 2):
                self.createWidget(gridLayout, y, x)

    def createWidget(self, obj, y, x):
        widget = getWidgetPayloadFrom(y, x)

        if 'http' in widget:
            obj.add_widget(AsyncImage(source=widget))
        else:
            label = Label(text=widget)
            if (doesWidgetHaveSizeAttribute(y, x)):
                label.font_size = (str(getWidgetSizeAttribute(y, x)) + 'dp')
            else:
                label.font_size = '25dp'

            if (doesWidgetHaveColourAttribute(y, x)):
                
                # Python is a silly goose, hex2Colour returns a tuple but they're immutable.
                #  so we make a new one with the (1,) then add that as the colour RGBA value
                rgba = colors.hex2color(('#' + str((getWidgetColourAttribute(y, x))))) + (1,)
                label.color = rgba

            obj.add_widget(label)


# Window.fullscreen = 'auto'
MirrorApplication().run()
