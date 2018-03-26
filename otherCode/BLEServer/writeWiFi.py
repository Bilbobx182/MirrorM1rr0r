import os
import json

path='/etc/wpa_supplicant/wpa_supplicant.conf'

with open(path) as fp:
	for line in fp:
		if(len(line) >2):
			print(len(line))
			print(line)

# Load JSON data

pathExtension = "/SMWSConfig.json"
currentPath = os.getcwd()
configPath = ((currentPath.split("mirrorRendering"))[0] + pathExtension)
configData = json.load(open(configPath))

with open(os.getcwd() + "/dummyConfig.conf", "a") as wifiConfigFile:
	wifiConfigFile.write("\n")
	wifiConfigFile.write("network={ \n")
	wifiConfigFile.write("	ssid=\"" + configData['WiFi'] + "\"" + "\n")
	wifiConfigFile.write("	pass=\"" + configData['Pass'] + "\"" + "\n")
	wifiConfigFile.write("	key_mgmt=WPA-PSK \n")
	wifiConfigFile.write("}")