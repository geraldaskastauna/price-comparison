//Import the express and url modules
var express = require('express');
var url = require("url");

//Status codes defined in external file
require('./http_status.js');

//The express module is a function. When it is executed it returns an app object
var app = express();

// Load the MySQL pool connection
const pool = require('./data/config');

// Display all users
app.get('/laptops', (request, response) => {
    pool.query('SELECT * FROM laptop', (error, result) => {
        if (error) throw error;

        response.send(result);
    });
});
