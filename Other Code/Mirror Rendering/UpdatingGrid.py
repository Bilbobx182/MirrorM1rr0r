from kivy.app import App
from kivy.uix.gridlayout import GridLayout
from kivy.clock import Clock
from kivy.uix.image import AsyncImage
from kivy.uix.label import Label
import os
import requests
import json

# I did this so the rows would be done correctly. Meaning if there's a message there we can make sure it's not occupied already that loop.
isOccupied = [[False, False, False], [False, False, False], [False, False, False]]
widgetsToRender = [
    [" ", " ", " "],
    [" ", " ", " "],
    [" ", " ", " "]]

base = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/getfifomessage"
queue = "?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo"
count = "&count=1"


# result = requests.get(base + queue + count).json()


def performRequest():
    result = requests.get(base + queue + count).json()
    if ('Message 0' in result):
        contents = json.loads(result['Message 0']['Contents'])

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


def falsifyOccupied():
    for y in (0, 1, 2):
        for x in (0, 1, 2):
            isOccupied[y][x] = False


class MirrorApplication(App):
    def build(self):
        gridLayout = GridLayout(cols=3, rows=3)
        # In seconds. NOTE later this will be read from a file
        updateInterval = 5

        self.create_button(gridLayout, "MIRRROR IS LOADING")
        Clock.schedule_interval(lambda a: self.update(gridLayout), updateInterval)
        return gridLayout

    def update(self, gridLayout):
        gridLayout.clear_widgets()
        performRequest()

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


MirrorApplication().run()
