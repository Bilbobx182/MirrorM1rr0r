from kivy.app import App
from kivy.uix.gridlayout import GridLayout
from kivy.uix.label import Label
from kivy.clock import Clock

import time


class popup(Label):
    def update(self, *args):
        self.text = "hello"


class TimeApp(App):
    def build(self):
        popupObject = popup()
        popupObject().open()
        Clock.schedule_once(popupObject.update(), 0)
        return popupObject

if __name__ == "__main__":
    TimeApp().run()