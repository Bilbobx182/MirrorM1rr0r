from kivy import Config
from kivy.app import App
from kivy.core.window import Window
from kivy.uix.button import Label, Button
import subprocess

from kivy.uix.gridlayout import GridLayout
from kivy.uix.image import AsyncImage
import requests


class MyApp(App):
    def getInformationFromQueue(self):
        url = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/getmessage?queue=https://sqs.eu-west-1.amazonaws.com/186314837751/hello&count=1"
        lm_json = requests.get(url).json()
        return lm_json['0']['Contents']

    def hello(self):
        print("hello")

    def build(self):
        layout = GridLayout(cols=3, rows=3)

        layout.add_widget(Label(text=''))

        layout.add_widget(AsyncImage(source=''))

        layout.add_widget(Label(text=''))
        layout.add_widget(Label(text=''))

        layout.add_widget(Label(text=''))
        layout.add_widget(Label(text=''))

        layout.add_widget(Label(text=''))
        layout.add_widget(Label(text=''))

        layout.add_widget(AsyncImage(source=''))

        return layout

Window.fullscreen = 'auto'
MyApp().run()
