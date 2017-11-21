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
    
    def build(self):
        layout = GridLayout(cols=3, rows=3)

        layout.add_widget(Label(text='HELLO I AM CIARAN'))
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

