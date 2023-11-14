<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        body,
        html,
        #allmap {
            width: 100%;
            height: 100%;
            overflow: hidden;
            margin: 0;
            font-family: "微软雅黑";
        }
    </style>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=qdEsGsdvptS3OUtVXqWLTlKDbPDUrMTl"></script>
    <!-- 密钥： 在百度地图开发者中心获取 -->
    <script src="http://libs.baidu.com/jquery/1.9.0/jquery.js"></script>
    <title>地图展示</title>
</head>
<body>
<div id="allmap"></div>
</body>
</html>
<script type="text/javascript">
    var baidumap = function () {
        var map;
        function init() {
            // 百度地图API功能
            map = new BMap.Map("allmap", { minZoom: 4, maxZoom: 15 }); // 创建Map实例,设置地图允许的最小/大级别(参数可选)
            map.centerAndZoom(new BMap.Point(116.404, 39.915), 6); // 初始化地图,设置中心点坐标和地图级别(数字越大，位置显示越精确)
            //添加地图类型控件
            map.addControl(new BMap.MapTypeControl({
                mapTypes: [
                    BMAP_NORMAL_MAP,
                    BMAP_HYBRID_MAP
                ]
            }));
            map.setCurrentCity("西安"); // 设置地图显示的城市 此项是必须设置的
            map.enableScrollWheelZoom(true); //开启鼠标滚轮缩放
            //标记地图位置
            function addMarker(point) {
                var marker = new BMap.Marker(point);
                map.addOverlay(marker);
            }
            var mapData = GetMapData(); //获取位置数据
            for (i = 0; i < mapData.length; i++) {
                var dataIndex = mapData[i];
                var point = new BMap.Point(dataIndex.Point.longitude, dataIndex.Point.Latitude);
                var marker = new BMap.Marker(point);
                map.addOverlay(marker);
                addClickHandler(dataIndex, marker);
            }
        }
        function addClickHandler(dataIndex, marker) {
            marker.addEventListener("mouseover", function (e) {
                openInfo(dataIndex, e)
            });
            marker.addEventListener("mousemove", function (e) {
                $('.BMap_pop>img').trigger('click');
                // map.closeInfoWindow(infoWindow,point); //开启信息窗口
            } );
        }
        function openInfo(dataIndex, e) {
            var p = e.target;
            var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
            var infoWindow = new BMap.InfoWindow(dataIndex.message, {
                width: 200, // 信息窗口宽度
                height: 100, // 信息窗口高度
                title: dataIndex.title, // 信息窗口标题
                enableMessage: true,//设置允许信息窗发送短息
                message: dataIndex.message
            }); // 创建信息窗口对象
            map.openInfoWindow(infoWindow, point); //开启信息窗口
        }
        function GetMapData() {
            var mapData = [
                { "key": "1", "name": "北京市市中心", "Point": { "longitude": 116.41667, "Latitude": 39.91667 }, "title": "北京市市中心", "message": "亲耐滴，欢迎来到北京市市中心喔~" },
                { "key": "2", "name": "上海市区", "Point": { "longitude": 121.43333, "Latitude": 34.50000 }, "title": "上海市区", "message": "亲耐滴，欢迎来到上海市区喔~" },
                { "key": "3", "name": "天津市区", "Point": { "longitude": 117.20000, "Latitude": 39.13333 }, "title": "天津市区", "message": "亲耐滴，欢迎来到天津市区喔~" },
            ]
            return mapData;
        }
        return {
            Init: init
        };
    }();
    $(function () {
        baidumap.Init();
    });
</script>