<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>London Map Visualization</title>

    <!-- DeckGL Library -->
    <script src="https://unpkg.com/deck.gl@latest/dist.min.js"></script>

    <!-- ChartJs Library -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <!-- Font -->
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Playfair+Display&display=swap" rel="stylesheet">

    <style type="text/css">
    body {
      margin: 0;
      padding: 0;
      height: 100%;
    }

    #container {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      display: flex;
    }

    #sidebar {
      width: 300px;
      background-color: #f0f0f0;
      padding: 20px;
    }

    .bottomText{
        position:absolute;
        bottom: 20px;
    }


    #sidebar h1 {
      font-family: 'Playfair Display', sans-serif;
      font-weight: bold;
      font-size: 24px;
      color: #000000;
    }

    #sidebar p {
      font-family: 'Playfair Display', sans-serif;
      font-weight: 100;
      font-size: 14px;
      color: #000000;
    }

    #sidebar hr {
        width: 100%;
        border: 0;
        border-top: 1px solid #000000;
    }

    #slider-container {
        font-size: 12px;
        font-family: 'Playfair Display', sans-serif;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    #slider-container input[type="range"] {
        flex-grow: 1;
        display: inline-block;
        vertical-align: middle;
    }

    #slider-container span {
        text-align: right;
        display: inline-block;
        vertical-align: top;
    }

    #cords-container{
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    #cords-container #coordinateBox {
        flex-grow: 1;
        display: inline-block;
        vertical-align: middle;
    }

    #coordinateBox {
        border: 0.3px solid rgb(67, 67, 67);
        padding: 1.5px;
        margin-right: 10px;
        font-size: 12px;
        display: inline-block;
        vertical-align: middle;
    }

    #cords-container #getLocation {
        font-family: 'Playfair Display', sans-serif;
        min-width: 80px;
        text-align: right;
        display: inline-block;
        vertical-align: middle;
    }

    #refreshButton {
        font-family: 'Playfair Display', sans-serif;
        font-size: 20px;
        font-weight: bold;
        color: #500778;
        display: inline-block;
        margin: 0 auto;
    }

    #fetchButton {
        font-family: 'Playfair Display', sans-serif;
        font-size: 20px;
        font-weight: bold;
        color: #500778;
        display: inline-block;
        margin: 0 auto;
    }

    #map {
      flex-grow: 1;
      position: relative;
      width: auto;
    }
    </style>
</head>

<body>
    <div id="container">
        <div id="sidebar">
          <a href="${basePath}/region/list">&larr; Homepage</a>
          <hr>
          <h1>Find the Safest Hotel</h1>
          <p>You can check the crime data around each hotel in London.</p>

          <div id="slider-container">
            <input type="range" id="slider" min="400" max="1500" value="750" step="50">
            <span>Radius: <span id="sliderValue">750</span> m</span>
          </div>

          <br>

          <div id="cords-container">
            <div id="coordinateBox">Please click to search location</div>
            <button id="getLocation">Get Location</button>
          </div>

          <br>

          <div id="search-container">
            <button id="refreshButton">Refresh Page</button>
            <button id="fetchButton">Search</button>
          </div>

          <br>

          <canvas id="scatterPlot" width="400" height="295"></canvas>

          <div class="bottomText">

            <h1>Links</h1>
            <a href="https://www.openstreetmap.org" class="link-style">OpenStreetMap</a>
            <p>Hotel data in OSM "Tourism" category</p>
            <a href="https://www.met.police.uk/sd/stats-and-data/" class="link-style">Metropolitan Police</a>
            <p>Crime data in the last 24 months up to 2023.09</p>
          </div>
        </div>

        <div id="map"></div>

      </div>

    <script type="text/JavaScript">

        document.getElementById('refreshButton').addEventListener('click', function() {
                location.reload();
        });

        // Link the slider to the display number
        document.getElementById('slider').oninput = function() {
            document.getElementById('sliderValue').textContent = this.value;
        };

        // Set parameter to check the click status
        let isLocationActive = false;
        // Make the GetLocation Button keep listening the click event
        document.getElementById('getLocation').addEventListener('click', () => {
            isLocationActive = true;
            document.getElementById('coordinateBox').textContent = 'Searching...';
        });

        // Set up the fetchButton to get data from api
        document.getElementById('fetchButton').addEventListener('click', function () {
            // Get the value of slider
            const radius = document.getElementById('slider').value;
            // Get the text from coords box
            const coordinateBoxText = document.getElementById('coordinateBox').textContent;
            // Get the Numbers in coords text
            const matches = coordinateBoxText.match(/-?\d+\.\d+/g);
            // Set the lon and lat
            const lon = matches[0];
            const lat = matches[1];

            // console.log("Latitude:", lat);
            // console.log("Longitude:", lon);


            // Set Url Link
            const apiUrl = `http://casa0017.cetools.org:8857/api/hotels/${lon}/${lat}/${radius}`;

            console.log(apiUrl);

            // Define asynchronous function for api fetching
            async function fetchData(apiUrl) {
                try {
                    const encodedUrl = encodeURI(apiUrl); // enCode the Url
                    // console.log('Encoded URL:', encodedUrl);
                    const response = await fetch(apiUrl);
                    if (!response.ok) {
                        throw new Error(`Network response was not ok, status: ${response.status}`);
                    }
                    const data = await response.json();
                    return data;
                } catch (error) {
                    console.error('API Request Fail:', error);
                    return [];
                }
            }

            let label = null; // Initialize the parameter for updating the label

            // Define asynchronous function for searching with APIs
            async function searchLayer(apiUrl) {
                const apiData = await fetchData(apiUrl);

                const pointLayer = new deck.ScatterplotLayer({
                    id: 'points',
                    data: apiData,
                    getPosition: (d) => [d.geo.x, d.geo.y], // Get the coords

                    // Styles
                    pointType: 'circle',
                    stroked: true,
                    filled: true,
                    lineWidthMinPixels: 1,
                    opacity: 0.5,
                    getRadius: 140,
                    getLineColor: [255, 255, 255], //RGB 0 - 255
                    getFillColor: [99, 0, 133],

                    onHover: ({ object, x, y }) => {
                    // Show label when the mouse stops upon
                        if (object) {
                            // if there is a label, delete the former one
                            if (label) {
                            label.remove();
                            }

                            // Create new page element
                            label = document.createElement('div');
                            label.textContent = object.name;
                            label.style.position = 'absolute';
                            label.style.left = `${x}px`;
                            label.style.top = `${y}px`;
                            document.body.appendChild(label);
                        } else {
                            if (label) {
                            label.remove();
                            label = null; // Reset Parameter
                            }
                        }
                    }
                });

                const lsoaUrl = `http://casa0017.cetools.org:8875/lsoa/${lon}/${lat}/${radius}`;

                console.log("LSOA URL:", lsoaUrl);

                // Request the LSOA API
                const lsoaData = await fetchData(lsoaUrl);
                const lsoaNums = lsoaData.map(item => item.LSOA_Num);

                // Set up an array for lsoa crime data storage
                let lsoaCrimeData = [];

                // console.log(lsoaNums);

                // Crime Data Processing
                for (const num of lsoaNums) {
                    const crimeUrl = `http://casa0017.cetools.org:8875/crime/${num}`;
                    const crimeData = await fetchData(crimeUrl);

                    // Sum up the data of different crime category
                    let crimeSum = crimeData.reduce((sum, crime) => sum + crime.TotalCrimeCount, 0);

                    // Update the array for crime data
                    lsoaCrimeData.push({ LSOA_Num: num, crimeSum: crimeSum });
                }

                // console.log(lsoaCrimeData);

                // Construct New Element For ScatterPlot
                const ctx = document.getElementById('scatterPlot').getContext('2d');
                // Initialize ScatterChart Object
                const scatterChart = new Chart(ctx, {
                    type: 'bubble',
                    data: {
                        datasets: [{
                            label: 'Crime Data',
                            data: lsoaCrimeData.map(item => {
                                return {
                                    x: item.LSOA_Num,
                                    y: item.crimeSum,
                                    r: Math.sqrt(item.crimeSum) / 5 // fit the bubble size and reduce the difference
                                };
                            }),
                            backgroundColor: '#500778'
                        }]
                    },
                    options: {
                            scales: {
                                x: {
                                    type: 'category',
                                    title: {
                                        display: true,
                                        text: 'LSOA Number',
                                        font: {
                                            size: 8
                                        }
                                    },
                                    ticks: {
                                        font: {
                                            size: 8
                                        }
                                    }
                                },
                                y: {
                                    beginAtZero: true,
                                    title: {
                                        display: true,
                                        text: 'Crime Count',
                                        font: {
                                            size: 8
                                        }
                                    },
                                    ticks: {
                                        font: {
                                            size: 8
                                        }
                                    }

                                }
                            },
                            plugins: {
                                legend: {
                                    display: false
                                }
                            }
                        }
                });

                const outerLayer = new deck.GeoJsonLayer({
                    id: 'outer',
                    data: outer,
                    // Styles
                    stroked: true,
                    filled: true,
                    lineWidthMinPixels: 0.10,
                    opacity: 0.9,
                    getLineColor: [0, 0, 0], //RGB 0 - 255
                    getFillColor: [69,39,104,215],
                });

                const lsoa1 = '../src/london_lsoa.geojson';

                const lsoaLayer = new deck.GeoJsonLayer({
                    id: 'lsoa1', //every layer needs to have a unique ID
                    data: lsoa1, //data can be passed as variable or added inline
                    // Styles
                    stroked: true,
                    filled: true,
                    lineWidthMinPixels: 0.28,
                    opacity: 1,
                    getLineColor: [0, 0, 0], //RGB 0 - 255
                    getFillColor: object => {
                        console.log('getFillColor called');
                        // 从 lsoa 数据中获取带有 "E0" 前缀的 LSOA11CD
                        const lsoaNumWithPrefix = object.properties.LSOA11CD;
                        // 从 lsoaNumWithPrefix 移除 "E0" 前缀
                        const lsoaNum = lsoaNumWithPrefix.startsWith('E0') ? lsoaNumWithPrefix.substring(2) : lsoaNumWithPrefix;
                        console.log(`LSOA Num: ${lsoaNum}`);
                        // 在 lsoaCrimeData 中查找匹配的记录
                        const crimeData = lsoaCrimeData.find(c => c.LSOA_Num === lsoaNum);

                        if (crimeData) {
                            // 根据犯罪数量调整颜色
                            const intensity = Math.min(1, crimeData.crimeSum / 10000); // 假设100为最高犯罪数
                            return [0, 0, 255 * (1 - intensity), 255]; // 深蓝到浅蓝
                        } else {
                            // 如果没有出现在犯罪数据中，使用默认颜色
                            return [255, 255, 255, 255]; // 白色
                        }
                    },

                    pickable:true,
                    onClick: (info) => {
                        if (isLocationActive && info.object) {
                            const [longitude, latitude] = info.coordinate;
                            const lon = Math.round(longitude * 100000) / 100000;
                            const lat = Math.round(latitude * 100000) / 100000;
                            document.getElementById('coordinateBox').textContent = `Lat: ${lat}, Lon: ${lon}`;
                            isLocationActive = false;
                        }
                    },
                });

                // const hotelLayer = new deck.GeoJsonLayer({
                //     id: 'hotels', //every layer needs to have a unique ID
                //     data: hotels, //data can be passed as variable or added inline
                //     pickable: true,
                //     // Styles
                //     pointType: 'circle',
                //     stroked: true,
                //     filled: true,
                //     lineWidthMinPixels: 1,
                //     opacity: 0.95,
                //     getRadius: 150,
                //     getLineColor: [0, 0, 0], //RGB 0 - 255
                //     getFillColor: [79, 75, 69],
                // });

            // 将点图层添加到 DeckGL 地图中
            deckgl.setProps({ layers: [outerLayer,lsoaLayer,pointLayer] });
            }
            // 调用函数创建点图层并添加到地图中
            searchLayer(apiUrl);
        });

        const hotels = '../src/london_hotel.geojson';

        const lsoa = '../src/london_lsoa.geojson';

        const outer = '../src/outer_update.geojson';

        const initialViewState =  {
                latitude: 51.51646544379633,
                longitude: -0.10846510533549451,
                zoom: 9,
                minZoom: 9,
                maxZoom: 13,
                bearing: 0,
                pitch: 0
        };

        const deckgl = new deck.DeckGL({
            container: 'map', // the id of the div element
            initialViewState: initialViewState,
            onViewStateChange: function ({ viewState }) {
                // Constraint the lat and lon
                viewState.longitude = Math.max(-0.2220, Math.min(0.0200, viewState.longitude));
                viewState.latitude = Math.max(51.4310, Math.min(51.5450, viewState.latitude));

                // Get zoom variable
                const zoom = deckgl._getViewState().zoom;;

                console.log(zoom);

                // Update View
                deckgl.setProps({ viewState: viewState });
            },

            parameters: {
                //Canvas background color, it can be applied to DIV CSS as well
                clearColor: [0, 0, 0, 0] //RGB 0-1+ opacity
            },

            controller: true, //activate the mouse control

            layers: [

                new deck.GeoJsonLayer({
                    id: 'outer',
                    data: outer,
                    // Styles
                    stroked: true,
                    filled: true,
                    lineWidthMinPixels: 0.10,
                    opacity: 0.9,
                    getLineColor: [0, 0, 0], //RGB 0 - 255
                    getFillColor: [69,39,104,215],
                }),

                new deck.GeoJsonLayer({
                    id: 'lsoa', //every layer needs to have a unique ID
                    data: lsoa, //data can be passed as variable or added inline
                    // Styles
                    stroked: true,
                    filled: true,
                    lineWidthMinPixels: 0.28,
                    opacity: 1,
                    getLineColor: [0, 0, 0], //RGB 0 - 255
                    getFillColor: [255,255,255],
                    pickable:true,
                    onClick: (info) => {
                        if (isLocationActive && info.object) {
                            const [longitude, latitude] = info.coordinate;
                            const lon = Math.round(longitude * 100000) / 100000;
                            const lat = Math.round(latitude * 100000) / 100000;
                            document.getElementById('coordinateBox').textContent = `Lat: ${lat}, Lon: ${lon}`;
                            isLocationActive = false;
                        }
                    },
                }),

                new deck.GeoJsonLayer({
                    id: 'hotels', //every layer needs to have a unique ID
                    data: hotels, //data can be passed as variable or added inline
                    pickable: true,
                    // Styles
                    pointType: 'circle',
                    stroked: true,
                    filled: true,
                    lineWidthMinPixels: 1,
                    opacity: 0.7,
                    getRadius: 150,
                    getLineColor: [0, 0, 0], //RGB 0 - 255
                    getFillColor: [79, 75, 69],
                }),
            ]
        });

    </script>
</body>
</html>