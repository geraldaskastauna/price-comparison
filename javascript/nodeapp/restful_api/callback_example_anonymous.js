//Call the function to do some work
doSomeWork(function (result){//The callback function is anonymous
    console.log("Work is completed. Result is " + result);
});

/* This function does something that takes a long time */
function doSomeWork(callBackFunction){
    //Some work that takes a bit of time
    var tmp = 0;
    for(var i=0; i<1000000000; i++){
        tmp += i;
    }
    //Call the callback function when the work is completed.
    callBackFunction(tmp);
}

