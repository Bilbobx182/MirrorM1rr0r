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
        i = 0
        while (i < 9):

            if (i == 0):
                nestedLayout = GridLayout(cols=2, rows=1)
                nestedLayout.add_widget(Label(text=" LEFT "))
                nestedLayout.add_widget(Label(text=" RIGHT "))
                layout.add_widget(nestedLayout)
            else:
                layout.add_widget(Label(text=" yo "))
            i += 1

        return layout


# Window.fullscreen = 'auto'
MyApp().run()
