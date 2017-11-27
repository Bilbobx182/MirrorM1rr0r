from kivy.app import App
from kivy.uix.gridlayout import GridLayout
from kivy.clock import Clock
from kivy.uix.image import AsyncImage
from kivy.uix.label import Label
import os
import requests
import json

"""
Widget Priority levels: 
0 : Decayable  (It will go away after X amount of minutes)
1 : Non-Decayable (This will only go away when you want it to)

Widgets that aren't going to decay get a timer of -1

That way we can check if the timer is -1 if it is go to next and check 
if it's time to delete that from the list and put a space in the related widgets to render.

"""

"""

I want to make it so that you can have priority.
Some widgets would never get overwritten and make any update to that location wait until the user says "delete"
But that's a lot of work. And decay is cooler.

So For now I am putting an occupied state commented away. in a TODO
# isOccupied = [[False, False, False], [False, False, False], [False, False, False]]

def falsifyOccupied():
    for y in (0, 1, 2):
        for x in (0, 1, 2):
            isOccupied[y][x] = False

"""


# TODO Make an occupied State


# result = requests.get(base + queue + count).json()

class MirrorApplication(App):
    widgetTimeToDecay = [[-1, -1, -1], [-1, -1, -1], [-1, -1, -1]]

    widgetsToRender = [
        [" ", " ", " "],
        [" ", " ", " "],
        [" ", " ", " "]]

    base = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/"
    queue = "https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo"

    # Default it to 1 just to be safe. I just arbitrarily decided this, no greater reason.
    count = 1

    def performRequest(self):
        countModifier = "&count="
        modifier = "getfifomessage?queueurl="
        result = requests.get(self.base + modifier + self.queue + countModifier + str(self.count)).json()
        messageCountLoop = 0

        while (messageCountLoop < self.count):
            messageKey = 'Message ' + str(messageCountLoop)
            if (messageKey in result):

                contents = json.loads(result[messageKey]['Contents'])

                if ('location' in contents):
                    location = contents['location']
                    yLocation = int(contents['location'].split(",")[0])
                    xLocation = int(contents['location'].split(",")[1])

                    self.widgetsToRender[yLocation][xLocation] = contents['messagePayload']

                # If no location is specified default it to the center.
                else:
                    self.widgetsToRender[1][1] = contents['messagePayload']

            messageCountLoop += 1

    def getMessageCount(self):
        modifier = "getmessagecount?queuename="
        return int(requests.get(self.base + modifier + self.queue.split("/")[::-1][0]).json())

    def build(self):
        gridLayout = GridLayout(cols=3, rows=3)
        # In seconds. NOTE later this will be read from a file
        updateInterval = 2

        self.create_widget(gridLayout, "MIRRROR IS LOADING")
        Clock.schedule_interval(lambda a: self.update(gridLayout), updateInterval)
        return gridLayout

    def update(self, gridLayout):
        if (self.getMessageCount() >= 1):

            gridLayout.clear_widgets()
            if (self.getMessageCount() > 9):
                self.count = 9
            else:
                self.count = self.getMessageCount()

            self.performRequest()

            for y in (0, 1, 2):
                for x in (0, 1, 2):
                    self.create_widget(gridLayout, self.widgetsToRender[y][x])

    def create_widget(self, obj, widgetToRender):
        if 'http' in widgetToRender:
            obj.add_widget(AsyncImage(source=widgetToRender))
        else:
            obj.add_widget(Label(text=widgetToRender))


MirrorApplication().run()
