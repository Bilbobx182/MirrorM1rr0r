var fs = require('fs');
var path = require('path');
var appDir = path.dirname(require.main.filename);
/*
Data Formats Expected to be written.

WiFi SSID : HOST
WiFi Pass : Hunter2
queueURL  : https://uniqueID.execute-api.region.amazonaws.com/confidence/

*/
fs.writeFile("input.csv", "WRITEING DATA!", function(errorHappened) {
    if(errorHappened) {
        return console.log(errorHappened);
    }

    console.log("The file was saved!");
}); 