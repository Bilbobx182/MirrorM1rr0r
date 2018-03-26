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

with open("/etc/wpa_supplicant/wpa_supplicant.conf", "w") as wifiConfigFile:

	wifiConfigFile.write("ctrl_interface=DIR=/var/run/wpa_supplicant GROUP=netdev\n")
	wifiConfigFile.write("update_config=1\n")
	wifiConfigFile.write("country=GB\n")
	wifiConfigFile.write("network={\n")
	wifiConfigFile.write("	ssid=\"" + configData['WiFi'] + "\"" + "\n")
	wifiConfigFile.write("	psk=\"" + configData['Pass'] + "\"" + "\n")
	wifiConfigFile.write("	key_mgmt=WPA-PSK\n")
	wifiConfigFile.write("}")