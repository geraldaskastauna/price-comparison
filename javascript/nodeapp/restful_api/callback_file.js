//Import file system functionality
var fs = require('fs');

console.log("About to read file.");

//Read number from file system - this is an asynchronous function
fs.readFile('test_file.txt', fileReadComplete);

//In a synchronous program this console log would occur after the file read had finished.
console.log("File read is complete.")

/* This is a callback function, which is called when the work is completed */
function fileReadComplete(err, result){
    console.log("Callback function: File read is completed. File contents: '" + result + "'.");
}



