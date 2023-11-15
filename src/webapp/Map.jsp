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
    <style type="text/css">
        body, html, #allmap {
            width: 100%;
            height: 100%;
            overflow: hidden;
            margin: 0;
        }
    </style>
    <script type="text/javascript"
            src="//api.map.baidu.com/api?type=webgl&v=1.0&ak=qdEsGsdvptS3OUtVXqWLTlKDbPDUrMTl"></script>
    <title>设置地图3D视角</title>
</head>
<body>
<div id="allmap"></div>
<div>
    <input type="hidden" value="${rest.longitude}" id="longitude"/>
    <input type="hidden" value="${rest.latitude}" id="latitude"/>
</div>
</body>
</html>
<script type="text/javascript">
    window.onload = function () {
        var longitude = $("#longitude").val();
        var latitude = $("#latitude").val();
        var map = new BMapGL.Map("allmap");
        // map.centerAndZoom(new BMapGL.Point(-0.12776, 51.50735), 19);
        map.centerAndZoom(new BMapGL.Point(longitude, latitude), 19);
        map.enableScrollWheelZoom(true);
        map.setHeading(64.5);
        map.setTilt(73);
    }
</script>