from kivy.app import App
from kivy.uix.gridlayout import GridLayout
from kivy.clock import Clock
import os

from kivy.uix.label import Label

# In seconds. NOTE later this will be read from a file
updateInterval = 5


class MirrorApplication(App):
    def build(self):
        gridLayout = GridLayout(cols=3)
        self.create_button(gridLayout, "MIRRROR IS LOADING")
        Clock.schedule_interval(lambda a: self.update(gridLayout), updateInterval)
        return gridLayout

    def update(self, gridLayout):
        gridLayout.clear_widgets()
        for y in (1, 2, 3):
            for x in (1, 2, 3):
                self.create_button(gridLayout,str(os.urandom(5)))

    def create_button(self, obj, input):
        obj.add_widget(Label(text=input))




MirrorApplication().run()
