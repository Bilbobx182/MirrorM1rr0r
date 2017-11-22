from kivy.app import App
from kivy.uix.gridlayout import GridLayout
from kivy.clock import Clock
from kivy.uix.label import Label
import os
import requests
import json

# In seconds. NOTE later this will be read from a file
updateInterval = 2

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

    contents = json.loads(result['Message 0']['Contents'])
    if ('location' in contents):
        location = contents['location']
        yLocation = int(contents['location'].split(",")[0])
        xLocation = int(contents['location'].split(",")[1])
        isOccupied[yLocation][xLocation] = True
        widgetsToRender[yLocation][xLocation] = contents['messagePayload']

    elif (isOccupied[1][1] == False):
        widgetsToRender[1][1] = contents['messagePayload']

    print(isOccupied)


class MirrorApplication(App):
    def build(self):
        gridLayout = GridLayout(cols=3, rows=3)
        self.create_button(gridLayout, "MIRRROR IS LOADING")
        Clock.schedule_interval(lambda a: self.update(gridLayout), updateInterval)
        return gridLayout

    def update(self, gridLayout):
        gridLayout.clear_widgets()
        for y in (1, 2, 3):
            for x in (1, 2, 3):
                self.create_button(gridLayout, str(os.urandom(5)))

    def create_button(self, obj, input):
        obj.add_widget(Label(text=input))


MirrorApplication().run()
