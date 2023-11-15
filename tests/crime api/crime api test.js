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


//  API EndPoint to get lsoa block around specific location - /data/51.1/0.0/30 
app.get('/lsoa/:lat/:lon/:radius', function (req, res) {

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

//  API EndPoint to get crime data within certain lsoa geometry - /crime/lsoaCode 
app.get('/crime/:lsoaCode', function (req, res) {

        // Alows data to be downloaded from the server with security concerns
        res.header("Access-Control-Allow-Origin", "*");
        res.header("Access-Control-Allow-Headers", "X-Requested-WithD");
        // If all the variables are provided connect to the database
        if(req.params.lsoaCode != ""){
                 
                  // Parse the values from the URL into numbers for the query
                  var lsoaCode = parseFloat(req.params.lsoaCode);  
  
                  // SQL Statement to run
                  var sql = "SELECT Crime_Category, SUM(\`2021_10\`+\`2021_11\`+\`2021_12\`+\`2022_01\`+\`2022_02\`+\`2022_03\`+\`2022_04\`+\`2022_05\`+\`2022_06\`+\`2022_07\`+\`2022_08\`+\`2022_09\`+\`2022_10\`+\`2022_11\`+\`2022_12\`+\`2023_01\`+\`2023_02\`+\`2023_03\`+\`2023_04\`+\`2023_05\`+\`2023_06\`+\`2023_07\`+\`2023_08\`+\`2023_09\`) AS TotalCrimeCount FROM crime_data WHERE LSOA_Code = 'E0"+lsoaCode+"' GROUP BY Crime_Category;";


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