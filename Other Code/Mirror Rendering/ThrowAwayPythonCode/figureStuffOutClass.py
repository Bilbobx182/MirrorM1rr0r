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
        layout = GridLayout(cols=4, rows=4)

        i = 0
        max = 16

        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))
        layout.add_widget(Button(text="hello", size_hint_x=None, width=int(Window.width) / 2, size_hint_y=None,
                                     height=int(Window.height) / 2))



        return layout

# Config.set('graphics', 'width', '1000')
# Config.set('graphics', 'height', '1000')
Window.fullscreen = 'auto'
MyApp().run()
