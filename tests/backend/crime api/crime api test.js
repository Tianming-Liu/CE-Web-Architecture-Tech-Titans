#!/usr/bin/env node

//  Crime Data API Server
//  Author:  Tianming Liu
//  Description:  This Crime Data API provides the crime data in the unit of LSOA.

var portNumber = 8875;

var mysql = require('mysql');

//  Setup the Express Server
var express = require('express');
var app = express();

// Import Private config for database

const config = require('config.js')

// MySQL Connection Variables
var connection = mysql.createConnection({
  host     : config.host,
  user     : config.user,
  password : config.password,
  database : config.database
});

// Initialize Connect to MySQL

connection.connect();


//  API EndPoint to get crime data around specific location - /data/51.1/0.0/30 
app.get('/data/:lat/:lon/:radius', function (req, res) {

      // Alows data to be downloaded from the server with security concerns
      res.header("Access-Control-Allow-Origin", "*");
      res.header("Access-Control-Allow-Headers", "X-Requested-WithD");
      // If all the variables are provided connect to the database
      if(req.params.lat != "" && req.params.lon != "" && req.params.radius != ""){
               
                // Parse the values from the URL into numbers for the query
                var lat = parseFloat(req.params.lat);
                var lon = parseFloat(req.params.lon);
                var radius = parseFloat(req.params.radius);


                // SQL Statement to run
                var sql = "SELECT LSOA_Code FROM lsoa WHERE ST_Distance_Sphere(ST_Centroid(geo), POINT("+lon+","+lat+")) <= "+radius;
                
                // Log it on the screen for debugging
                console.log(sql);

                // Run the SQL Query
                connection.query(sql, function(err, rows, fields) {
                        if (err) console.log("Err:" + err);
                        if(rows != undefined){
                                // If we have data that comes bag send it to the user.
                                res.send(rows);
                        }else{
                                res.send("");
                        }
                });
        }else{
                // If all the URL variables are not passed send an empty string to the user
                res.send("");
        }
});


// Setup the server and print a string to the screen when server is ready
var server = app.listen(portNumber, function () {
  var host = server.address().address;
  var port = server.address().port;
  console.log('App listening at http://%s:%s', host, port);
});