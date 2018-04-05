# Smart Mirror Widget System Repo

## Description:
	Hey, wouldn't it be great to have a mirror you could render almost anything on? Even better wouldn't it be better so that _anyone_ could figure out how to use it?
	Well that's this projects one floor elevator pitch.
	The use cases are endless. The only limit is your imagination.
	
	A whole report has been made, if you want detailed information feel free to read it, it will be added to the repo soon.

## How it works:
Follow this flowchart, it will help you understand how everything works together, it leaves out the cloud implementation as it'd make it too busy.
It took me forever to do so it's worth following through :) 

![Flowchart of how it works](https://i.imgur.com/XdHa0vU.png)

## Technology:
### Mirror:
	->Kivy : Rendering widgets onto the mirror
	->BLENO: BLE Gatt server so that wifi can be programatically set

### Cloud:
	AWS:
	-> API Gateway	: Routes incoming request to the stateless lambda functions
	-> Lambda		: Runs stateless code, such as sending or getting messages from a queue.
	-> SQS			: Used to keep messages so the mirror can request them.
	
### Android:
-> Phone:
	-> RXAndroidBLE	: Used for easier BLE communication in Android
	-> MessageAPI	: Used for sending messages to Android Wear.
	


## Devices used:
-> Nokia 3
-> Raspberry Pi 3B NOOBS