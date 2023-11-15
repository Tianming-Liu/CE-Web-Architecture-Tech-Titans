<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% pageContext.setAttribute("basePath", request.getContextPath()); %>
<script type="text/javascript" src="${basePath}/static/js/jquery-3.6.0.min.js"></script>
<link href="${basePath}/static/bootstrap-3.4.1-dist/css/bootstrap.css" rel="stylesheet">
<script src="${basePath}/static/bootstrap-3.4.1-dist/js/bootstrap.min.js"></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Hotel Information</title>
</head>
<body>
<div class="container1">
    <div style="width:12%;text-align:left;margin-left:30px;margin-right:30px;">
        <br/><br/><br/>
        <ul class="nav nav-pills nav-stacked">
            <li><a href="${basePath}/region/list">London Street</a></li>
            <br>
            <li><a href="${basePath}/classification/list">Crime Classification</a></li>
            <br>
            <li class="active"><a href="${basePath}/hotel/list">Hotel Information</a></li>
        </ul>
    </div>
    <div style="width:88%;text-align: left;margin-left:20px;margin-right:30px;">
        <div>
            <nav class="navbar navbar-default" role="navigation">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <a class="navbar-brand" href="http://casa0017.cetools.org/~group6/mapSearch.html">Hotel & Crime Map</a>
                    </div>
                    <ul class="nav navbar-nav navbar-right">
                        <li id="logout" style="margin-top:15px;margin-right: 30px;"><span
                                class="glyphicon glyphicon-user"></span>Sign Out
                        </li>
                    </ul>
                </div>
            </nav>
        </div>
        <div class="row">
            <div class="col-md-3">
                <div class="input-group">
                    <span class="input-group-addon">Name</span>
                    <input type="text" class="form-control" placeholder="" id="name" style="width: 200px;">
                </div>
            </div>
            <div class="col-md-3">
                <div class="input-group">
                    <span class="input-group-addon">Region</span>
                    <input type="text" class="form-control" placeholder="" id="region" style="width: 200px;">
                </div>
            </div>
            <div class="col-md-3">
                <button type="button" class="btn btn-default" style="width: 100px;margin-right:10px;"
                        onclick="search(this)">Search
                </button>
                <button type="button" class="btn btn-default" style="width: 100px;margin-left: 10px;"
                        onclick="reset(this)">Reset
                </button>
            </div>
        </div>
        <br>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>No.</th>
                <th>Hotel</th>
                <th>Longitude</th>
                <th>Latitude</th>
                <th>Region</th>
                <th>Crime Amount</th>
                <th>Map</th>
            </tr>
            </thead>
            <tbody id="content">
            <c:forEach items="${data.list}" var="item">
                <tr>
                    <td>${item.number}</td>
                    <td>${item.name}</td>
                    <td>${item.longitude}</td>
                    <td>${item.latitude}</td>
                    <td>${item.regionName}</td>
                    <td>${item.value}</td>
                    <td>
                        <button type="button" class="btn btn-danger" onclick="edit_obj(this)"
                                data-url="${item.uid}">
                            Map
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div>
            <ul class="pager">
                <li><span id="lastPage">Last Page</span></li>
                &nbsp;&nbsp;&nbsp;&nbsp;<span>
                The</span><span id="currentPage">${data.page.currentPage}</span><span>page&nbsp;&nbsp;All</span>
                <span id="total">${data.page.total}</span><span>page</span>
                </span>&nbsp;&nbsp;&nbsp;&nbsp;
                <li><span id="nextPage">Next Page</span></li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>
<script type="text/javascript">
    $("#logout").click(function () {
        window.location.href = "${basePath}/logout";
    });

    $("#nextPage").click(function () {
        let currentPage = $("#currentPage").html();
        let total = $("#total").html();
        if (currentPage == total) {
            return;
        }
        let name = $("#name").val();
        let region = $("#region").val();
        let param = {
            "name": name,
            "region": region,
            "currentPage": Number(currentPage) + 1,
            "pageSize": 10
        }
        let jsonParam = JSON.stringify(param);
        list(jsonParam);
    });

    $("#lastPage").click(function () {
        let currentPage = $("#currentPage").html();
        if (currentPage == "1") {
            return;
        }
        let name = $("#name").val();
        let region = $("#region").val();
        let param = {
            "name": name,
            "region": regionf,
            "currentPage": Number(currentPage) - 1,
            "pageSize": 10
        }
        let jsonParam = JSON.stringify(param);
        list(jsonParam);
    });

    function search(obj) {
        var currentPage = $("#currentPage").html();
        let name = $("#name").val();
        let region = $("#region").val();
        let param = {
            "name": name,
            "region": region,
            "currentPage": currentPage,
            "pageSize": 10
        }
        let jsonParam = JSON.stringify(param);
        list(jsonParam);
    }

    function reset(obj) {
        $("#name").val("");
        $("#region").val("");
        let name = $("#name").val();
        let region = $("#region").val();
        let param = {
            "name": name,
            "region": region,
            "currentPage": 1,
            "pageSize": 10
        }
        let jsonParam = JSON.stringify(param);
        list(jsonParam);
    }

    function list(jsonParam) {
        $.ajax({
            url: '${basePath}/rest/hotel/list',//地址
            dataType: 'json',//数据类型
            type: 'POST',//类型
            timeout: 5000,//超时
            data: jsonParam,
            contentType: "application/json",
            //请求成功
            success: function (data) {
                if (data.returnCode !== '200') {
                    alert(data.message);
                    return;
                }
                console.log('data ' + data.returnData.page.total);
                $("#total").html(data.returnData.page.total);
                $("#currentPage").html(data.returnData.page.currentPage);
                initList(data.returnData.list);
            },
            error: function (e) {
                console.log("e:" + e);
            }
        });
    }

    function initList(data) {
        $("#content").empty();
        var choices = "";
        $.each(data, function (n, value) {
            var choice = '<tr><td>' + value.number + '</td><td>' + value.name + '</td><td>' + value.longitude + '</td><td>' + value.latitude + '</td>'
                + '<td>' + value.regionName + '</td><td>' + value.value + '</td>'
                + '<td><button type="button" onclick="edit_obj(this)" class="btn btn-default" data-url="' + value.uid + '">地图</button></td></tr>'
            choices += choice;
        });
        $("#content").append(choices);
    }

    function edit_obj(obj) {
        var uid = $(obj).attr("data-url");
        window.location.href = "${basePath}/map?uid=" + uid;
    }

    function delete_obj(obj) {
        var uid = $(obj).attr("data-url");
        let param = {
            uid: uid
        }
        let jsonParam = JSON.stringify(param);
        $.ajax({
            url: '${basePath}/rest/hotel/delete',//地址
            dataType: 'json',//数据类型
            type: 'POST',//类型
            timeout: 5000,//超时
            data: jsonParam,
            contentType: "application/json",
            //请求成功
            success: function (data) {
                if (data.returnCode !== '200') {
                    alert(data.message);
                    return;
                }
                reset(obj);
            }
        });
    }
</script>
<style type="text/css">
    .container1 {
        display: flex;
        /*justify-content: space-around;*/
    }
</style>
