//Import the express and url modules
var express = require('express');
var url = require("url");

//Status codes defined in external file
require('./http_status.js');

//The express module is a function. When it is executed it returns an app object
var app = express();

//Import the mysql module
var mysql = require('mysql');

//Create a connection object with the user details
var connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "",
    database: "price_comparison",
    debug: false
});

//Set up the application to handle GET requests sent to the user path
app.get('/cereals/*', handleGetRequest);//Subfolders
app.get('/cereals', handleGetRequest);

//Serve up static pages from public folder
app.use(express.static('public'));

//Start the app listening on port 8080
app.listen(8080);


/* Handles GET requests sent to web service.
   Processes path and query string and calls appropriate functions to
   return the data. */
function handleGetRequest(request, response){
    //Parse the URL
    var urlObj = url.parse(request.url, true);

    //Extract object containing queries from URL object.
    var queries = urlObj.query;

    //Get the pagination properties if they have been set. Will be  undefined if not set.
    var numItems = queries['num_items'];
    var offset = queries['offset'];

    //Split the path of the request into its components
    var pathArray = urlObj.pathname.split("/");

    //Get the last part of the path
    var pathEnd = pathArray[pathArray.length - 1];

    //If path ends with 'cereals' we return all cereals
    if(pathEnd === 'cereals'){
        getTotalCerealsCount(response, numItems, offset);//This function calls the getAllCereals function in its callback
        return;
    }

    //If path ends with cereals/, we return all cereals
    if (pathEnd === '' && pathArray[pathArray.length - 2] === 'cereals'){
        getTotalCerealsCount(response, numItems, offset);//This function calls the getAllCereals function in its callback
        return;
    }

    //If the last part of the path is a valid user id, return data about that user
    var regEx = new RegExp('^[0-9]+$');//RegEx returns true if string is all digits.
    if(regEx.test(pathEnd)){
        getCereal(response, pathEnd);
        return;
    }

    //The path is not recognized. Return an error message
    response.status(HTTP_STATUS.NOT_FOUND);
    response.send("{error: 'Path not recognized', url: " + request.url + "}");
}


/** Returns all of the cereals, possibly with a limit on the total number of items returned and the offset (to
 *  enable pagination). This function should be called in the callback of getTotalCerealsCount  */
function getAllCereals(response, totNumItems, numItems, offset){
    //Select the cereals data using JOIN to convert foreign keys into useful data.
    var sql = "SELECT cereals.id, brands.name, product_type.name, product_type.description, cereals.weight, cereals.price " +
    "FROM ( (cereals INNER JOIN product_type ON cereals.product_type_id=product_type.id) " +
    "INNER JOIN brands ON cereals.brand_id=brands.id ) " +
    "ORDER BY cereals.id ";

    //Limit the number of results returned, if this has been specified in the query string
    if(numItems !== undefined && offset !== undefined ){
        sql += "LIMIT " + numItems + " OFFSET " + offset;
    }

    //Execute the query
    connectionPool.query(sql, function (err, result) {

        //Check for errors
        if (err){
            //Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        //Create JavaScript object that combines total number of items with data
        var returnObj = {totNumItems: totNumItems};
        returnObj.cereals = result; //Array of data from database

        //Return results in JSON format
        response.json(returnObj);
    });
}


/** When retrieving all cereals we start by retrieving the total number of cereals
    The database callback function will then call the function to get the cereal data
    with pagination */
function getTotalCerealsCount(response, numItems, offset){
    var sql = "SELECT COUNT(*) FROM cereals";

    //Execute the query and call the anonymous callback function.
    connectionPool.query(sql, function (err, result) {

        //Check for errors
        if (err){
            //Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        //Get the total number of items from the result
        var totNumItems = result[0]['COUNT(*)'];

        //Call the function that retrieves all cereals
        getAllCereals(response, totNumItems, numItems, offset);
    });
}


/** Returns the cereal with the specified ID */
function getCereal(response, id){
    //Build SQL query to select cereal with specified id.
    var sql = "SELECT cereals.id, brands.name, product_type.name, product_type.description, cereals.weight, cereals.price " +
        "FROM ( (cereals INNER JOIN product_type ON cereals.product_type_id=product_type.id) " +
        "INNER JOIN brands ON cereals.brand_id=brands.id ) " +
        "WHERE cereals.id=" + id;

    //Execute the query
    connectionPool.query(sql, function (err, result) {

        //Check for errors
        if (err){
            //Not an ideal error code, but we don't know what has gone wrong.
            response.status(HTTP_STATUS.INTERNAL_SERVER_ERROR);
            response.json({'error': true, 'message': + err});
            return;
        }

        //Output results in JSON format
        response.json(result);
    });
}
