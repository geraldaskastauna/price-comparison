//Import file system functionality
var fs = require('fs');

console.log("About to read file.");

//The promise contains code that reads number from file system - this is an asynchronous function
//The Promise is resolved when the file callback function is called.
var filePromise = new Promise(function(resolve, reject){
    fs.readFile('test_file.txt', function(err, result) {
        console.log("Callback function: File read is completed. File contents: '" + result + "'.");
        resolve("File read done");
    });
});


//Promise.then() is called after the functionality within the promise has been resolved
filePromise.then(function(){
    console.log("File read is complete.")
});

