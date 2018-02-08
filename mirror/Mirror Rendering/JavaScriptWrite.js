var fs = require('fs');
var path = require('path');

/*
Program Helper: Expected data

Data Formats Expected to be written.
Item,Data,
WiFi SSID, HOST
WiFi Pass, Hunter2
queueURL, https://uniqueID.execute-api.region.amazonaws.com/confidence/

*/


// Sample CSV data, remove this when mergeing with other code.
var csvData = ["IsThisTheKrustyKrab", "Hunter2", "https://uniqueID.execute-api.region.amazonaws.com/confidence/"];

// Perform check after a message get's sent call this method.
function checkDataSent() {
    if (csvData.length >= 2) {
        csvData.push("Ciaran");
        performCSVWrite();
    }
    else {
        csvData.push("Ciaran");
        checkDataSent();
    }
}

performCSVWrite();

function performCSVWrite() {
    // Mix the data with the headers we need for CSV
    combined = combineData();

    fs.writeFile("input.csv", combined, function (errorHappened) {
        if (errorHappened) {
            return console.log(errorHappened);
        }

        console.log("The file was saved!");
    });
}

function combineData() {
    var comibned = [];
    var csvHeaders = ["SSID", "Pass", "queueURL"];

    var counter = 0;

    while (counter < 3) {
        comibned.push(csvHeaders[counter] + "," + csvData[counter]);
        counter = counter + 1;
    }
    console.log(comibned);
    return comibned;
}