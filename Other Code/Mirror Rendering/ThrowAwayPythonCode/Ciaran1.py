import json
from pprint import pprint
import os
pathExtension = "Bluetooth Low Energy Server\SMWSConfig.json"
currentPath = os.getcwd()

configPath = ((currentPath.split("Mirror Rendering"))[0] + pathExtension)
data = json.load(open(configPath))

pprint(data)