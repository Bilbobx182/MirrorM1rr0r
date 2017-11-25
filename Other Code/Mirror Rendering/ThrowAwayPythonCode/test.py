import datetime
'''

Problem: I want to have a decay timer on items. This check shouldn't be done on the UI thread.
The changes it makes however should affect the UI THREAD

0) Make a global variable
1) Make a thread
2) Get state of varaible
3) Update varaible if needed


'''

value = "Hello"
valueDecayTime = -1

def setValue():
    global value
    value = "Ciaran"
    print(datetime.datetime.now().time())
    print(str(datetime.datetime.now() + datetime.timedelta(minutes=15)).split(" ")[::-1][0])

print(value)
setValue()
print(value)