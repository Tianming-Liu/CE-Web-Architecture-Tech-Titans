<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% pageContext.setAttribute("basePath", request.getContextPath()); %>
<script type="text/javascript" src="${basePath}/static/js/jquery-3.6.0.min.js"></script>
<link href="${basePath}/static/bootstrap-3.4.1-dist/css/bootstrap.css" rel="stylesheet">
<script src="${basePath}/static/bootstrap-3.4.1-dist/js/bootstrap.min.js"></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <script type="text/javascript"
            src="//api.map.baidu.com/api?type=webgl&v=1.0&ak=qdEsGsdvptS3OUtVXqWLTlKDbPDUrMTl"></script>
    <title>Search Result</title>
    <style>
        body,
        html,
        #container {
            overflow: hidden;
            width: 100%;
            height: 100%;
            margin: 0;
            font-family: "微软雅黑";
        }

        .info {
            z-index: 999;
            width: auto;
            min-width: 22rem;
            padding: .75rem 1.25rem;
            margin-left: 1.25rem;
            position: fixed;
            top: 1rem;
            background-color: #fff;
            border-radius: .25rem;
            font-size: 14px;
            color: #666;
            box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
        }
    </style>
</head>
<body>
<div id="container"></div>
<div class="info">Result of ${rest.name}</div>
<div>
    <input type="hidden" value="${rest.longitude}" id="longitude"/>
    <input type="hidden" value="${rest.latitude}" id="latitude"/>
    <input type="hidden" value="${rest.name}" id="name"/>
    <input type="hidden" value="${color}" id="color"/>
</div>
</body>
</html>
<script type="text/javascript">
    window.onload = function () {
        var longitude = $("#longitude").val();
        var latitude = $("#latitude").val();
        var name = $("#name").val();
        var color = $("#color").val();
        // BaiduAPI
        var map = new BMapGL.Map("container");            // Create Map
        var mPoint = new BMapGL.Point(longitude, latitude);
        map.enableScrollWheelZoom();
        map.centerAndZoom(mPoint, 15);

        var circle = new BMapGL.Circle(mPoint, 1000, {
            fillColor: color,
            strokeWeight: 1,
            fillOpacity: 0.3,
            strokeOpacity: 0.3
        });
        map.addOverlay(circle);
        var local = new BMapGL.LocalSearch(map, {renderOptions: {map: map, autoViewport: false}});
        local.searchNearby(name, mPoint, 1000);
    }
</script>
