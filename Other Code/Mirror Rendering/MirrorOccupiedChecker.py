import boto3
import requests
import json
from kivy.app import App

from kivy.core.window import Window
from kivy.uix.gridlayout import GridLayout
from kivy.uix.image import AsyncImage
from kivy.uix.label import Label


def populateGrid():
    print("populating....")

# I did this so the rows would be done correctly. Meaning if there's a message there we can make sure it's not occupied already that loop.
isOccupied = [[False, False, False], [False, False, False], [False, False, False]]
widgetsToRender = [
    [".", ".", "."],
    [".", ".", "."],
    [".", ".", "."]]

# Deal with the case of a message not having a location later


base = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/getfifomessage"
queue = "?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo"
count = "&count=1"

result = requests.get(base + queue + count).json()

contents = json.loads(result['Message 0']['Contents'])
if('location' in contents):
    location = contents['location']
    yLocation = int(contents['location'].split(",")[0])
    xLocation = int(contents['location'].split(",")[1])
    isOccupied[yLocation][xLocation] = True
    widgetsToRender[yLocation][xLocation] = contents['messagePayload']

elif(isOccupied[1][1] == False):
    widgetsToRender[1][1] = contents['messagePayload']

print(isOccupied)


class MyApp(App):
    def build(self):
        layout = GridLayout(cols=3, rows=3)

        loop = 0
        x = 0
        y = 0
        while (loop < 9):
            if 'http' in widgetsToRender[y][x]:
                layout.add_widget(AsyncImage(source=widgetsToRender[y][x]))
            else:
                layout.add_widget(Label(text=widgetsToRender[y][x]))

            if (x == 2):
                x = 0
                y += 1
            else:
                x += 1
            loop += 1
        return layout

Window.fullscreen = 'auto'
MyApp().run()
