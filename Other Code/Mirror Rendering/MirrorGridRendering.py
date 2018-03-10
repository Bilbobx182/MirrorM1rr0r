import json

import requests
from kivy.app import App
from kivy.clock import Clock
from kivy.core.window import Window
from kivy.uix.gridlayout import GridLayout
from kivy.uix.image import AsyncImage
from kivy.uix.label import Label

# I did this so the rows would be done correctly. Meaning if there's a message there we can make sure it's not occupied already that loop.
isOccupied = [[False, False, False], [False, False, False], [False, False, False]]
widgetsToRender = [
    [" ", " ", " "],
    [" ", " ", " "],
    [" ", " ", " "]]
messageCount = 1
base = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/getfifomessage"
queue = "?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo"
count = "&count=" + str(messageCount)


# result = requests.get(base + queue + count).json()


def performRequest():
    result = requests.get(base + queue + count).json()

    messageKey = 'Message 0'
    if (messageKey in result):

        contents = json.loads(result[messageKey]['Contents'])

        if (contents['messagePayload'].startswith("/")):
            performCommand(contents['messagePayload'])

        if ('location' in contents):
            location = contents['location']
            yLocation = int(contents['location'].split(",")[0])
            xLocation = int(contents['location'].split(",")[1])

            isOccupied[yLocation][xLocation] = True
            widgetsToRender[yLocation][xLocation] = contents['messagePayload']

        # If no location is specified default it to the center.
        elif (isOccupied[1][1] == False):
            widgetsToRender[1][1] = contents['messagePayload']


print(isOccupied)


def clearMirror():
    global widgetsToRender  # Needed to modify global copy of globvar


widgetsToRender = [
    [" ", " ", " "],
    [" ", " ", " "],
    [" ", " ", " "]]


def performCommand(payload):
    if ("clear" in payload):
        clearMirror()


def falsifyOccupied():
    for y in (0, 1, 2):
        for x in (0, 1, 2):
            isOccupied[y][x] = False

clearCount = 0

class MirrorApplication(App):
    def build(self):
        gridLayout = GridLayout(cols=3, rows=3)
        # In seconds. NOTE later this will be read from a file
        updateInterval = 5

        self.create_button(gridLayout, "MIRRROR IS LOADING")
        Clock.schedule_interval(lambda a: self.update(gridLayout), updateInterval)
        return gridLayout

    def update(self, gridLayout):
        global clearCount
        gridLayout.clear_widgets()
        if(clearCount < 2):
            performRequest(self)
            clearCount+=1
        else:
            clearCount = 0
            clearMirror()
        for y in (0, 1, 2):
            for x in (0, 1, 2):
                self.create_button(gridLayout, widgetsToRender[y][x])

        # Need to mark the area as empty so any new item that comes in can over-ride what was previosuly there
        # This is done so that items within the same loop can't mess each other up but new requests are fine to over-write

        falsifyOccupied()

    def create_button(self, obj, widgetToRender):
        if 'http' in widgetToRender:
            obj.add_widget(AsyncImage(source=widgetToRender))
        else:
            obj.add_widget(Label(text=widgetToRender))


# Window.fullscreen = 'auto'
MirrorApplication().run()
