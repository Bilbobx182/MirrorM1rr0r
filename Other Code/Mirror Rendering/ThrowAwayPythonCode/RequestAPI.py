data = "weather.main"
import requests

weatherAPI = list()
weatherAPI.append("http://api.openweathermap.org/data/2.5/weather?")
weatherAPI.append("lat=53.35&lon=-6.26")
weatherAPI.append("&units=metric&APPID=c050be8146f9067def4aabdd5c51b98b")





JSONresult = requests.get(''.join(weatherAPI)).json()


result = {}
result['max'] = JSONresult['main']['temp_max']
result['type'] = JSONresult['weather'][0]['description']