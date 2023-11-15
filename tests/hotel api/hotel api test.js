#!/usr/bin/env node

// Hotel Data API Server
// Author: Huarui Yang
// Description: This Hotel Data API provides hotel data in London.

var portNumber = 8857;

var mysql = require('mysql');

// Setup the Express Server
var express = require('express');
var app = express();

// MySQL Connection Variables
var connection = mysql.createConnection({
    host: 'casa0017.cetools.org',
    user: 'ucfnhyo',
    password: 'yourpassword',
    database: 'ucfnhyo',
});

// Initialize connection to MySQL
connection.connect();

// API endpoint to retrieve hotel data based on location and radius
app.get('/api/hotels/:lat/:lon/:radius', function (req, res) {

    // Allow data to be downloaded from the server with security concerns
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "X-Requested-With");

    // If all the variables are provided, connect to the database
    if (req.params.lat !== "" && req.params.lon !== "" && req.params.radius !== "") {

        // Parse the values from the URL into numbers for the query
        var lat = parseFloat(req.params.lat);
        var lon = parseFloat(req.params.lon);
        var radius = parseFloat(req.params.radius);

        // SQL Statement to run
        var sql = "SELECT * FROM \`London hotel info\` WHERE ST_Distance_Sphere(ST_Centroid(geo), POINT(" + lon + "," + lat + ")) <= " + radius;

        // Log it on the screen for debugging
        console.log(sql);

        // Run the SQL Query
        connection.query(sql, function (err, rows, fields) {
            if (err) console.log("Error: " + err);
            if (rows !== undefined) {
                // If we have data, send it to the user.
                res.send(rows);
            } else {
                res.send("");
            }
        });
    } else {
        // If all the URL variables are not passed, send an empty string to the user
        res.send("");
    }
});

// Setup the server and print a string to the screen when the server is ready
var server = app.listen(portNumber, function () {
    var host = server.address().address;
    var port = server.address().port;
    console.log('App listening at http://%s:%s', host, port);
});
