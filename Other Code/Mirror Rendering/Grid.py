from kivy.app import App
from kivy.core.window import Window
from kivy.uix.button import Label, Button
import subprocess

from kivy.uix.gridlayout import GridLayout
from kivy.uix.image import AsyncImage


class MyApp(App):
    def build(self):
        Window.fullscreen = 'auto'
        # return Label(text="hello", pos=((1920/2) *-.98, 0))
        layout = GridLayout(cols=3, rows=3)

        layout.add_widget(Button(text='Side Top LEFT'))
        layout.add_widget(Button(text='CT'))

        layout.add_widget(Button(text='Side Top RIGHT'))
        layout.add_widget(Button(text='CL'))

        layout.add_widget(Button(text='CM'))
        layout.add_widget(Button(text='CR'))

        layout.add_widget(Button(text='SIDE BOT LEFT'))
        layout.add_widget(Button(text='CB'))
        layout.add_widget(AsyncImage(source='https://media.giphy.com/media/aWPGuTlDqq2yc/giphy.gif'))
        return layout

MyApp().run()
