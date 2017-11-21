import boto3
import requests
import json
from kivy.uix.gridlayout import GridLayout
from kivy.uix.image import AsyncImage
from kivy.uix.label import Label


def populateGrid():
    print("populating....")


isOccupied = [[False, False, False], [False, False, False], [False, False, False]]

# Deal with the case of a message not having a location later


base = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/getfifomessage"
queue = "?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo"
count = "&count=1"

result = requests.get(base + queue + count).json()

contents = json.loads(result['Message 0']['Contents'])
location = contents['location']

yLocation = int(contents['location'].split(",")[0])
xLocation = int(contents['location'].split(",")[1])

isOccupied[yLocation][xLocation] = True

print(isOccupied)

# def build(self):
#     layout = GridLayout(cols=3, rows=3)
#
#     result = self.getInformationFromQueue()
#     if (result[0]['Message'].startswith('https')):
#         layout.add_widget(AsyncImage(source=result[0]['Message']))
#     else:
#         layout.add_widget(Label(text=(result[0]['Message'])))
#
#     layout.add_widget(Label(text=''))
#     layout.add_widget(Label(text=''))
#     layout.add_widget(Label(text=''))
#     layout.add_widget(Label(text=''))
#     layout.add_widget(Label(text=''))
#     layout.add_widget(Label(text=''))
#     layout.add_widget(Label(text=''))
#     layout.add_widget(Label(text=''))

print(result)
