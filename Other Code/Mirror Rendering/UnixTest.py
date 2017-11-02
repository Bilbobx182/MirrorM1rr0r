#
# def getMonitorDimensionsUnix():
#     #   proc = subprocess.Popen('fbset', stdout=subprocess.PIPE)
#     #   rawResult = proc.stdout.read()
#     rawResult = ' b\nmode "1920x1080"\n    geometry 1920 1080 1920 1080 32\n    timings 0 0 0 0 0 0 0\n    rgba 8/16,8/8,8/0,8/24\nendmode\n\n'
#     xLocation = rawResult.index('x')
#     return (rawResult[xLocation-4:xLocation+5])
#
# print(getMonitorDimensionsUnix())