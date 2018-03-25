import os.path
import sqlite3

widgetsMongoObjectIdentifiers = [
    [" ", " ", " "],
    [" ", " ", " "],
    [" ", " ", " "]]

currentPath = os.path.dirname(os.path.abspath(__file__))
dbPath = currentPath.split("mirrorRendering")[0] + "SMWS.db"
conn = sqlite3.connect(dbPath)
c = conn.cursor()


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


def getWidgetColourAttribute(y, x):
    widgetJSON = c.execute(
        'SELECT fontColour FROM Message WHERE ID = ' + str(widgetsMongoObjectIdentifiers[y][x]) + ';')
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


def getWidgetPayloadFrom(y, x):
    widgetJSON = c.execute(
        'SELECT messagePayload FROM Message WHERE ID = ' + str(widgetsMongoObjectIdentifiers[y][x]) + ';')
    for result in widgetJSON:
        return result[0]


def getDynamicWidgetContents(y, x):
    widgetJSON = c.execute(
        'SELECT command,extraMessage,lat,long FROM Message WHERE ID = ' + str(
            widgetsMongoObjectIdentifiers[y][x]) + ';')
    for result in widgetJSON:
        out = {}
        out['command'] = result[0]
        out['extraMessage'] = result[1]
        out['lat'] = result[2]
        out['long'] = result[3]
        return out


test = "'"


def getDynamicWidgetContents(y, x):
    messagePayload = "Hi"
    fontColour = "EMPTY"
    fontSize = "EMPTY"
    isDynamic = "EMPTY"
    command = "EMPTY"
    extraMessage = "EMPTY"
    lat = "EMPTY"
    long = "EMPTY"

    c.execute(
        "UPDATE Message SET messagePayload = ? ,fontColour = ? ,fontSize = ?,isDynamic= ?, command = ?,extraMessage= ?,lat= ?,long= ? WHERE ID = ?;",
        (messagePayload, fontColour, fontSize, isDynamic, command, extraMessage, lat, long,
         widgetsMongoObjectIdentifiers[y][x]))
    conn.commit()


getAndSetWidgetIDs()
print(widgetsMongoObjectIdentifiers)
print(doesWidgetHaveSizeAttribute(1, 0))
print(doesWidgetHaveColourAttribute(0, 0))
print("COLOUR : " + getWidgetColourAttribute(1, 0))
print(isDynamicWidget(1, 0))
print(getWidgetPayloadFrom(1, 0))

getDynamicWidgetContents(2, 0)
