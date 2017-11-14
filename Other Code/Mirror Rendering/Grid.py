from kivy import Config
from kivy.app import App
from kivy.core.window import Window
from kivy.uix.button import Label, Button
import subprocess
import operator
import json

from kivy.uix.gridlayout import GridLayout
from kivy.uix.image import AsyncImage
import requests


class MyApp(App):
    def getInformationFromQueue(self):
        url = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/getmessage?queue=https://sqs.eu-west-1.amazonaws.com/186314837751/hellociaran&count=2"
        lm_json = requests.get(url).json()

        contents = []
        for key, value in lm_json.items():
            item = {}
            item['Message'] = value['Contents']
            item['Timestamp'] = int(value['SentTimestamp'])
            contents.append(item);

        return sorted(contents, key=operator.itemgetter('Message', 'Timestamp'))

    def hello(self):
        print("hello")

    def build(self):
        layout = GridLayout(cols=3, rows=3)

        result = self.getInformationFromQueue()

        layout.add_widget(Label(text=''))

        layout.add_widget(Label(text=''))

        layout.add_widget(Label(text=''))
        layout.add_widget(Label(text=''))

        layout.add_widget(Label(text=''))
        layout.add_widget(Label(text=''))

        layout.add_widget(Label(text=''))
        layout.add_widget(Label(text=''))

        layout.add_widget(Label(text=''))

        return layout


Window.fullscreen = 'auto'
MyApp().run()
