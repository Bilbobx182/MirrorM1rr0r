import datetime
from threading import Thread
from time import sleep
import time
import _thread

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


class decayState():
    def print_time(self, threadName, delay):
        count = 0
        while count < 5:
            time.sleep(delay)
            print("%s: %s" % (threadName, datetime.datetime.now().time()))
            count += 1

        print("I'm done")

    def main(self):
        try:
            _thread.start_new_thread(self.print_time, ("BackgroundThread", 2))
        except:
            print("Error: unable to start thread")
        while 1:
            pass


def setValue():
    global value
    value = "Ciaran"
    print(datetime.datetime.now().time())
    print(str(datetime.datetime.now() + datetime.timedelta(minutes=15)).split(" ")[::-1][0])


print(value)
setValue()
print(value)
decayTimer = decayState().main()
