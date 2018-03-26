var util = require('util');
var bleno = require('bleno');
var fs = require('fs');

var BlenoPrimaryService = bleno.PrimaryService;
var BlenoCharacteristic = bleno.Characteristic;
var BlenoDescriptor = bleno.Descriptor;
var chunkedJSON = "";
console.log('bleno');

var WriteOnlyCharacteristic = function() {
  WriteOnlyCharacteristic.super_.call(this, {
    uuid: 'fffffffffffffffffffffffffffffff4',
    properties: ['write', 'writeWithoutResponse']
  });
};

util.inherits(WriteOnlyCharacteristic, BlenoCharacteristic);

WriteOnlyCharacteristic.prototype.onWriteRequest = function(data, offset, withoutResponse, callback) {
  console.log('WriteOnlyCharacteristic write request: ' + data.toString());
	console.log(data.toString());
	chunkedJSON = chunkedJSON + data.toString();
	if(data.toString().indexOf('}') > -1){
		writeInputToFile(chunkedJSON);
	}
  callback(this.RESULT_SUCCESS);
};


var sys = require('sys')
var exec = require('child_process').exec;
var child;


function writeInputToFile(data){
	fs.writeFile("SMWSConfig.json",data.toString(),function (error) {
		if(error) {
		//Error handling I will do later
		}
		else {
			console.log("Info Dumped");
			console.log(data.toString());
			child = exec("cd /home/pi/MirrorM1rr0r/otherCode/BLEServer && ./updateMirrorPref.sh", function (error, stdout, stderr) {
			sys.print('stdout: ' + stdout);
  			sys.print('stderr: ' + stderr);
  				if (error !== null) {
    				console.log('exec error: ' + error);
  				}
			});
		}
	});
}

function SampleService() {
  SampleService.super_.call(this, {
    uuid: 'fffffffffffffffffffffffffffffff0',
    characteristics: [
      new WriteOnlyCharacteristic()
    ]
  });
}

bleno.on('disconnect', function(clientAddress) {
  console.log('on -> disconnect, client: ' + clientAddress);
	writeInputToFile(chunkedJSON);
});

/*  Reference BLENO Test code.
	Last Visited 1/March/2018
	Source : https://github.com/noble/bleno/blob/master/test.js
	*/

util.inherits(SampleService, BlenoPrimaryService);

bleno.on('stateChange', function(state) {
  console.log('on -> stateChange: ' + state + ', address = ' + bleno.address);

  if (state === 'poweredOn') {
    bleno.startAdvertising('SMWS', ['fffffffffffffffffffffffffffffff0']);
  } else {
    bleno.stopAdvertising();
  }
});

// Linux only events /////////////////
bleno.on('accept', function(clientAddress) {
  console.log('on -> accept, client: ' + clientAddress);

  bleno.updateRssi();
});

bleno.on('rssiUpdate', function(rssi) {
  console.log('on -> rssiUpdate: ' + rssi);
});
//////////////////////////////////////

bleno.on('mtuChange', function(mtu) {
  console.log('on -> mtuChange: ' + mtu);
});

bleno.on('advertisingStart', function(error) {
  console.log('on -> advertisingStart: ' + (error ? 'error ' + error : 'success'));

  if (!error) {
    bleno.setServices([
      new SampleService()
    ]);
  }
});

bleno.on('advertisingStop', function() {
  console.log('on -> advertisingStop');
});

bleno.on('servicesSet', function(error) {
  console.log('on -> servicesSet: ' + (error ? 'error ' + error : 'success'));
});

//END reference