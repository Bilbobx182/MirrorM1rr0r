from kivy.app import App
from kivy.core.window import Window
from kivy.uix.button import Label

from kivy.uix.gridlayout import GridLayout

# This is concept code for a grid that has nested grids.
# Although, I am aware that this is opening a can of worms for development.
# I fear it may not be worth it to implement but interesting idea nonetheless.


class MyApp(App):
    def build(self):
        layout = GridLayout(cols=3, rows=3)
        i = 0
        while (i < 9):

            if (i == 0):
                nestedLayout = GridLayout(cols=1, rows=2)
                nestedLayout.add_widget(Label(text=" LEFT "))
                nestedLayout.add_widget(Label(text=" RIGHT "))
                layout.add_widget(nestedLayout)
            else:
                layout.add_widget(Label(text=" yo "))
            i += 1

        return layout


Window.fullscreen = 'auto'
MyApp().run()
