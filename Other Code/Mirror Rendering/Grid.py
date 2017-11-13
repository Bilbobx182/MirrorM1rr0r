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

        import json
        import boto3

        def lambda_handler(event, context):
            sqs = boto3.client('sqs')
            response = sqs.send_message(QueueUrl=event['queryStringParameters']['queueurl'],
                                        MessageBody=event['queryStringParameters']['message'], MessageAttributes={
                    'MessageGroupId': "messageID"
                })

            out = {}
            out['statusCode'] = 200
            out['body'] = "Success"
            out['headers'] = {
                "content-type": "application-json"
            }

            return (out)

        layout.add_widget(Label(text=''))

        layout.add_widget(Label(text='NIGGA IS HERE YO THIS IS REA REALLLY'))

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
