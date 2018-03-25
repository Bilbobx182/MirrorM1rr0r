import json
import os
import sqlite3
import struct

import requests
from kivy.app import App
from kivy.clock import Clock
from kivy.uix.gridlayout import GridLayout
from kivy.uix.image import AsyncImage, Image
from kivy.uix.label import Label

currentPath = os.path.dirname(os.path.abspath(__file__))
dbPath = currentPath.split("mirrorRendering")[0] + "SMWS.db"
conn = sqlite3.connect(dbPath)
c = conn.cursor()

widgetsMongoObjectIdentifiers = [
    [" ", " ", " "],
    [" ", " ", " "],
    [" ", " ", " "]]

messageCount = 1

pathExtension = "BLEServer\SMWSConfig.json"
currentPath = os.getcwd()

configPath = ((currentPath.split("mirrorRendering"))[0] + pathExtension)
configData = json.load(open(configPath))

base = "https://trbcvi749b.execute-api.eu-west-1.amazonaws.com/Prod/getmessage"
queue = "?queueurl=" + configData['queue']

count = "&count=" + str(messageCount)


def getAndSetWidgetIDs():
    y = 0
    x = 0
    global widgetsMongoObjectIdentifiers

    results = c.execute('SELECT * FROM Message;')

    for result in results:
        print(result)

        widgetsMongoObjectIdentifiers[y][x] = result[0]
        x += 1
        if (x == 3):
            x = 0
            y += 1
            if (y == 3):
                y = 0


def getWidgetPayloadFrom(y, x):
    widgetJSON = c.execute(
        'SELECT messagePayload FROM Message WHERE ID = ' + str(widgetsMongoObjectIdentifiers[y][x]) + ';')
    for result in widgetJSON:
        return result[0]


def doesWidgetHaveColourAttribute(y, x):
    widgetJSON = c.execute(
        'SELECT fontColour FROM Message WHERE ID = ' + str(widgetsMongoObjectIdentifiers[y][x]) + ';')
    for result in widgetJSON:
        if (result[0] == 'EMPTY'):
            return False
        else:
            return True


def doesWidgetHaveSizeAttribute(y, x):
    widgetJSON = c.execute(
        'SELECT fontSize FROM Message WHERE ID = ' + str(widgetsMongoObjectIdentifiers[y][x]) + ';')
    for result in widgetJSON:
        if (result[0] == 'EMPTY'):
            return False
        else:
            return True


def getWidgetColourAttribute(y, x):
    widgetJSON = c.execute(
        'SELECT fontColour FROM Message WHERE ID = ' + str(widgetsMongoObjectIdentifiers[y][x]) + ';')
    for result in widgetJSON:
        return result[0]


def getWidgetSizeAttribute(y, x):
    widgetJSON = c.execute(
        'SELECT fontSize FROM Message WHERE ID = ' + str(widgetsMongoObjectIdentifiers[y][x]) + ';')
    for result in widgetJSON:
        return result[0]


def isDynamicWidget(y, x):
    widgetJSON = c.execute(
        'SELECT isDynamic FROM Message WHERE ID = ' + str(widgetsMongoObjectIdentifiers[y][x]) + ';')
    for result in widgetJSON:

        if (result[0].lower() == 'true'):
            return True
        else:
            return False


def getDynamicWidgetContents(y, x):
    widgetJSON = c.execute(
        'SELECT *  FROM Message WHERE ID = ' + str(
            widgetsMongoObjectIdentifiers[y][x]) + ';')
    for result in widgetJSON:
        out = {}
        out['messagePayload'] = result[1]
        out['fontColour'] = result[2]
        out['fontSize'] = result[3]
        out['isDynamic'] = result[4]

        dynamicContents = {}
        dynamicContents['command'] = result[5]
        dynamicContents['extraMessage'] = result[6]
        dynamicContents['lat'] = result[7]
        dynamicContents['long'] = result[8]

        out['dynamicIdentifier'] = dynamicContents

        return out


def updateWidget(jsonContents):
    yLocation = 1
    xLocation = 1

    fontSize = ""
    fontColour = "ffffff"

    if ('location' in jsonContents):
        yLocation = int(jsonContents['location'].split(",")[0])
        xLocation = int(jsonContents['location'].split(",")[1])

    if ('fontColour' in jsonContents):
        fontColour = jsonContents['fontColour']

    if ('fontSize' in jsonContents):
        fontSize = jsonContents['fontSize']
    else:
        fontSize = 25

    c.execute(
        "UPDATE Message SET messagePayload = ? ,fontColour = ? ,fontSize = ?,isDynamic= ?, command = ?,extraMessage= ?,lat= ?,long= ? WHERE ID = ?;",
        (jsonContents['messagePayload'], fontColour, fontSize, "false", "EMPTY", "EMPTY", "EMPTY", "EMPTY",
         widgetsMongoObjectIdentifiers[yLocation][xLocation]))
    conn.commit()

    print(jsonContents)


def setup():
    getAndSetWidgetIDs()


def performRequest(gridLayout):
    result = requests.get(base + queue + count).json()

    messageKey = 'Message 0'

    if (messageKey in result):
        if "messagePayload" in json.loads(result[messageKey]['Contents']):
            payload = json.loads(result[messageKey]['Contents'])
            if ("^/^" in payload['messagePayload']):
                parseCommand(json.loads(result[messageKey]['Contents']), gridLayout)
            else:
                updateWidget(json.loads(result[messageKey]['Contents']))


def parseCommand(jsonCommand, gridLayout):
    if "^/^clear" in jsonCommand['messagePayload']:
        print("clearing Mirror")
        gridLayout.clear_widgets()

    if "^/^weather" in jsonCommand['messagePayload']:
        print("Getting weather")
        setWeatherWidget(jsonCommand)

    if "^/^tempature" in jsonCommand['messagePayload']:
        print("Getting current Temp")
        setTempatureWidget(jsonCommand)


def parseDynamicCommand(jsonCommand, gridLayout):
    if "^/^clear" in jsonCommand['dynamicIdentifier']['command']:
        print("clearing Mirror")
        gridLayout.clear_widgets()

    if "^/^weather" in jsonCommand['dynamicIdentifier']['command']:
        print("Getting weather")
        setWeatherWidget(jsonCommand)

    if "^/^temperature" in jsonCommand['dynamicIdentifier']['command']:
        print("Getting current Temp")
        setTempatureWidget(jsonCommand)


def setWeatherWidget(json):
    weatherAPI = list()
    # Sunny, Cloudy, Overcast, Rain
    weatherImages = ["https://i.imgur.com/OGPHWZZ.png", "https://i.imgur.com/NbnlGbw.png",
                     "https://i.imgur.com/uIC2Io8.png", "https://i.imgur.com/GWJ85t3.png"]

    weatherAPI.append("http://api.openweathermap.org/data/2.5/weather?")
    # Default location of Dublin
    weatherAPI.append("lat=53.35&lon=-6.26")
    weatherAPI.append("&units=metric&APPID=c050be8146f9067def4aabdd5c51b98b")

    # update the default to the lat and long the user supplies
    isDynamicBool = 'dynamicIdentifier' in json and ("EMPTY" not in json['dynamicIdentifier']['command'])
    if (isDynamicBool):
        weatherAPI[1] = "lat=" + json['dynamicIdentifier']['lat'] + "&lon=" + json['dynamicIdentifier']['lat']
    else:
        weatherAPI[1] = "lat=" + json['lat'] + "&lon=" + json['long']

    JSONresult = requests.get(''.join(weatherAPI)).json()

    result = {}
    result['max'] = JSONresult['main']['temp_max']
    result['type'] = JSONresult['weather'][0]['main']

    outJSON = {}

    if "Clear" in result['type']:
        outJSON['messagePayload'] = weatherImages[0]
    if "Cloud" in result['type']:
        outJSON['messagePayload'] = weatherImages[1]
    if "Mist" in result['type']:
        outJSON['messagePayload'] = weatherImages[1]
    if "Rain" in result['type']:
        outJSON['messagePayload'] = weatherImages[3]
    if "Drizzle" in result['type']:
        outJSON['messagePayload'] = weatherImages[3]

    if ('location' in json):
        outJSON['location'] = json['location']

    if (isDynamicBool):
        dynamicUpdateOutJSON = {'command': json['dynamicIdentifier']['command'],
                                'lat': json['dynamicIdentifier']['lat'],
                                'long': json['dynamicIdentifier']['long']}
    else:
        dynamicUpdateOutJSON = {'command': json['messagePayload'], 'lat': json['lat'], 'long': json['long']}

    outJSON['dynamicIdentifier'] = dynamicUpdateOutJSON

    updateDynamicWidget(outJSON)


def setTempatureWidget(json):
    weatherAPI = list()
    weatherAPI.append("http://api.openweathermap.org/data/2.5/weather?")
    # Default location of Dublin
    weatherAPI.append("lat=53.35&lon=-6.26")
    weatherAPI.append("&units=metric&APPID=c050be8146f9067def4aabdd5c51b98b")

    # update the default to the lat and long the user supplies
    isDynamicBool = 'dynamicIdentifier' in json and ("EMPTY" not in json['dynamicIdentifier']['command'])
    if (isDynamicBool):
        weatherAPI[1] = "lat=" + json['dynamicIdentifier']['lat'] + "&lon=" + json['dynamicIdentifier']['long']
    else:
        weatherAPI[1] = "lat=" + json['lat'] + "&lon=" + json['long']

    JSONresult = requests.get(''.join(weatherAPI)).json()

    result = {}
    result['max'] = JSONresult['main']['temp_max']

    outJSON = {}
    outJSON['messagePayload'] = str(result['max']) + "C"
    if ('location' in json):
        outJSON['location'] = json['location']

    if (isDynamicBool):
        dynamicUpdateOutJSON = {'command': json['messagePayload'], 'lat': json['dynamicIdentifier']['lat'],
                                'long': json['dynamicIdentifier']['long']}
    else:
        dynamicUpdateOutJSON = {'command': json['messagePayload'], 'lat': json['lat'], 'long': json['long']}
    outJSON['dynamicIdentifier'] = dynamicUpdateOutJSON
    updateDynamicWidget(outJSON)


def updateDynamicWidget(jsonContents):
    yLocation = 1
    xLocation = 1

    fontColour = "ffffff"

    extraMessage = " "
    lat = " "
    long = " "

    if ('location' in jsonContents):
        yLocation = int(jsonContents['location'].split(",")[0])
        xLocation = int(jsonContents['location'].split(",")[1])

    if ('fontColour' in jsonContents):
        fontColour = jsonContents['fontColour']

    if ('fontSize' in jsonContents):
        fontSize = jsonContents['fontSize']
    else:
        fontSize = 25

    command = jsonContents['dynamicIdentifier']['command']

    if ('lat' in jsonContents['dynamicIdentifier']):
        lat = jsonContents['dynamicIdentifier']['lat']

    if ('long' in jsonContents['dynamicIdentifier']):
        long = jsonContents['dynamicIdentifier']['long']

    if ('extraMessage' in jsonContents['dynamicIdentifier']):
        extraMessage = jsonContents['dynamicIdentifier']['extraMessage']

    c.execute(
        "UPDATE Message SET messagePayload = ? ,fontColour = ? ,fontSize = ?,isDynamic= ?, command = ?,extraMessage= ?,lat= ?,long= ? WHERE ID = ?;",
        (jsonContents['messagePayload'], fontColour, fontSize, "true", command, extraMessage, lat, long,
         widgetsMongoObjectIdentifiers[yLocation][xLocation]))
    conn.commit()

    print("DYNAMIC UPDATED")
    print(jsonContents)


def isConnectedToNetwork():
    try:
        # Because BBC is almost always up, and since it's large enough a ping to it wouldn't hurt them I ping them.
        response = requests.get('http://www.bbc.com/')
        if requests.codes.ok == response.status_code:
            return True

    except requests.exceptions.ConnectionError:
        return False


updateTimerCurrentValue = 0


class MirrorApplication(App):
    def build(self):
        setup()

        gridLayout = GridLayout(cols=3, rows=3)

        if (isConnectedToNetwork()):
            gridLayout.add_widget(Label(text="Loading now!"))
        else:
            label = Label(text="No network detected, Reboot please :(")
            label.font_size = "30dp"
            gridLayout.add_widget(label)
            gridLayout.add_widget(Image(source='networkError.png'))
            return gridLayout

        updateInterval = 5
        Clock.schedule_interval(lambda a: self.update(gridLayout), updateInterval)
        return gridLayout

    def update(self, gridLayout):
        gridLayout.clear_widgets()

        global updateTimerCurrentValue
        updateTimerCurrentValue += 5
        print("UPDATE TIMER : " + str(updateTimerCurrentValue))

        performRequest(gridLayout)

        for y in (0, 1, 2):
            for x in (0, 1, 2):
                self.createWidget(gridLayout, y, x)

    def createWidget(self, obj, y, x):

        if (isDynamicWidget(y, x)):
            global updateTimerCurrentValue
            updateTimerMaxValue = 15
            if (updateTimerCurrentValue >= updateTimerMaxValue):
                parseDynamicCommand(getDynamicWidgetContents(y, x), obj)
                updateTimerCurrentValue = 0

        widget = getWidgetPayloadFrom(y, x)

        if 'http' in widget:
            obj.add_widget(AsyncImage(source=widget))

        else:
            label = Label(text=widget)
            if doesWidgetHaveSizeAttribute(y, x):
                label.font_size = (str(getWidgetSizeAttribute(y, x)) + 'dp')
            else:
                label.font_size = '25dp'

            if doesWidgetHaveColourAttribute(y, x):
                fullRGB = struct.unpack('BBB', bytes.fromhex(getWidgetColourAttribute(y, x)))
                RGBA = []

                for colour in fullRGB:
                    RGBA.append(colour / 255)
                RGBA.append(1)
                label.color = RGBA

            obj.add_widget(label)

# Window.fullscreen = 'auto'
MirrorApplication().run()
