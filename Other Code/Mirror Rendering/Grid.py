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
        url = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/getmessage?count=1"
        lm_json = requests.get(url).json()
        return lm_json['0']['Contents']

    def hello(self):
        print("hello")
    def build(self):
        Config.set('graphics', 'width', '500')
        Config.set('graphics', 'height', '500')


        layout = GridLayout(cols=3, rows=3)

        layout.add_widget(AsyncImage(source=MyApp.getInformationFromQueue(self)))
        layout.add_widget(Label(text='CT'))

        layout.add_widget(Label(text='Side Top RIGHT'))
        layout.add_widget(Label(text='CL'))

        layout.add_widget(Label(text='CM'))
        layout.add_widget(Label(text='CR'))

        layout.add_widget(Label(text='SIDE BOT LEFT'))
        layout.add_widget(Label(text='CB'))
        layout.add_widget(AsyncImage(source=MyApp.getInformationFromQueue(self)))
        return layout


MyApp().run()
