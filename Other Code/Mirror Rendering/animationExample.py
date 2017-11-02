from kivy.app import App
from kivy.core.window import Window
from kivy.uix.button import Label
import subprocess


class MyApp(App):
    def build(self):
        Window.fullscreen = 'auto'
        # return Label(text="hello", pos=((1920/2) *-.98, 0))
        return Label(text="hello",pos=((1920/2)*.98, (1080/2)*.98))

MyApp().run()
