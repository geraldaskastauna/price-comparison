//Call the function to do some work
//We pass the callback function as an argument to the work function.
doSomeWork(workCompleted);

/* This is a callback function, which is called when the work is completed */
function workCompleted(result){
    console.log("Work is completed. Result is " + result);
}

/* This function does something that takes a long time */
function doSomeWork(callBackFunction){
    //Some work that takes a bit of time
    var sum = 0;
    for(var i=0; i<1000000000; i++){
        sum += i;
    }
    //Call the callback function when the work is completed.
    callBackFunction(sum);
}


