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
    connectionLimit: 15,
    host: "localhost",
    user: "root",
    password: "root",
    database: "price_comparison",
    insecureAuth: true,
    debug: false
});

app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "http://localhost:8080"); // update to match the domain you will make the request from
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});

var sql = "SELECT laptop.id, product.brand, product.description, product.image_url, url.domain, url.query_string, laptop.price " +
"FROM ( (laptop INNER JOIN product ON laptop.product_id=product.id) " +
"INNER JOIN url ON laptop.url_id=url.id ) " +
"ORDER BY laptop.id";
app.get('/laptops', (req, res)=>{
    connectionPool.query(sql, (err, rows, fields) => {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    });
});

app.get('/laptops/:id', (req, res)=>{
    connectionPool.query('SELECT * FROM laptop WHERE id = ?',[req.params.id] , (err, rows, fields)=> {
        if (!err)
            res.send(rows);
        else
            console.log(err);
    });
});

app.listen(3000,()=>console.log('Express server is running at 3000'));
