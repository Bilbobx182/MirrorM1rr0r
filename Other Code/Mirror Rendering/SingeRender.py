from kivy import Config
from kivy.app import App
from kivy.core.window import Window
from kivy.uix.button import Label, Button
import subprocess
import operator
import json
import time

from kivy.uix.gridlayout import GridLayout
from kivy.uix.image import AsyncImage
import requests


class MyApp(App):
    def getInformationFromQueue(self):

        baseurl = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/getmessage?"
        queueParameter = "queue="
        queueValue = "https://sqs.eu-west-1.amazonaws.com/186314837751/normalQueue"
        countParameter = "&count="
        countValue = "1"

        lm_json = requests.get(baseurl+queueParameter+queueValue+countParameter+countValue).json()

        contents = []
        for key, value in lm_json.items():
            item = {}
            item['Message'] = value['Contents']
            item['Timestamp'] = int(value['SentTimestamp'])
            contents.append(item);

        return sorted(contents, key=operator.itemgetter('Message', 'Timestamp'))

    def build(self):
        layout = GridLayout(cols=3, rows=3)

        result = self.getInformationFromQueue()
        if (result[0]['Message'].startswith('https')):
            layout.add_widget(AsyncImage(source=result[0]['Message']))
        else:
            layout.add_widget(Label(text=(result[0]['Message'])))
        return layout


Window.fullscreen = 'auto'
MyApp().run()

