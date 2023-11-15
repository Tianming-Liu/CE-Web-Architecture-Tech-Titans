<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% pageContext.setAttribute("basePath", request.getContextPath()); %>
<script type="text/javascript" src="${basePath}/static/js/jquery-3.6.0.min.js"></script>
<link href="${basePath}/static/bootstrap-3.4.1-dist/css/bootstrap.css" rel="stylesheet">
<script src="${basePath}/static/bootstrap-3.4.1-dist/js/bootstrap.min.js"></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<!-- https://codepen.io/danielkvist/pen/LYNVyPL -->
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <style>
        :root {
            /* COLORS */
            --white: #e9e9e9;
            --gray: #333;
            --blue: #0367a6;
            --lightblue: #008997;

            /* RADII */
            /*--button-radius: 0.7rem;*/

            /* SIZES */
            --max-width: 758px;
            --max-height: 420px;

            font-size: 16px;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
            Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
        }

        body {
            align-items: center;
            background-color: var(--white);
            /*background: url("https://res.cloudinary.com/dbhnlktrv/image/upload/v1599997626/background_oeuhe7.jpg");*/
            /* 决定背景图像的位置是在视口内固定，或者随着包含它的区块滚动。 */
            /* https://developer.mozilla.org/zh-CN/docs/Web/CSS/background-attachment */
            background-attachment: fixed;
            background-position: center;
            background-repeat: no-repeat;
            background-size: cover;
            display: grid;
            height: 100vh;
            place-items: center;
        }

        .form__title {
            font-weight: 300;
            margin: 0;
            margin-bottom: 1.25rem;
        }

        .link {
            color: var(--gray);
            font-size: 0.9rem;
            margin: 1.5rem 0;
            text-decoration: none;
        }

        .container {
            background-color: var(--white);
            /*border-radius: var(--button-radius);*/
            box-shadow: 0 0.9rem 1.7rem rgba(0, 0, 0, 0.25),
            0 0.7rem 0.7rem rgba(0, 0, 0, 0.22);
            height: var(--max-height);
            max-width: var(--max-width);
            overflow: hidden;
            position: relative;
            width: 100%;
        }

        .container__form {
            height: 100%;
            position: absolute;
            top: 0;
            transition: all 0.6s ease-in-out;
        }

        .container--signin {
            left: 0;
            width: 50%;
            z-index: 2;
        }

        .container.right-panel-active .container--signin {
            transform: translateX(100%);
        }

        .container--signup {
            left: 0;
            opacity: 0;
            width: 50%;
            z-index: 1;
        }

        .container.right-panel-active .container--signup {
            animation: show 0.6s;
            opacity: 1;
            transform: translateX(100%);
            z-index: 5;
        }

        .container__overlay {
            height: 100%;
            left: 50%;
            overflow: hidden;
            position: absolute;
            top: 0;
            transition: transform 0.6s ease-in-out;
            width: 50%;
            z-index: 100;
        }

        .container.right-panel-active .container__overlay {
            transform: translateX(-100%);
        }

        .overlay {
            background-color: var(--lightblue);
            background: url("https://cdn.pixabay.com/photo/2018/08/14/13/23/ocean-3605547_1280.jpg");
            background-attachment: fixed;
            background-position: center;
            background-repeat: no-repeat;
            background-size: cover;
            height: 100%;
            left: -100%;
            position: relative;
            transform: translateX(0);
            transition: transform 0.6s ease-in-out;
            width: 200%;
        }

        .container.right-panel-active .overlay {
            transform: translateX(50%);
        }

        .overlay__panel {
            align-items: center;
            display: flex;
            flex-direction: column;
            height: 100%;
            justify-content: center;
            position: absolute;
            text-align: center;
            top: 0;
            transform: translateX(0);
            transition: transform 0.6s ease-in-out;
            width: 50%;
        }

        .overlay--left {
            transform: translateX(-20%);
        }

        .container.right-panel-active .overlay--left {
            transform: translateX(0);
        }

        .overlay--right {
            right: 0;
            transform: translateX(0);
        }

        .container.right-panel-active .overlay--right {
            transform: translateX(20%);
        }

        .form {
            background-color: var(--white);
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
            padding: 0 3rem;
            height: 100%;
            text-align: center;
        }

        .input {
            background-color: #fff;
            border: none;
            padding: 0.9rem 0.9rem;
            margin: 0.5rem 0;
            width: 100%;
        }

        @keyframes show {

            0%,
            49.99% {
                opacity: 0;
                z-index: 1;
            }

            50%,
            100% {
                opacity: 1;
                z-index: 5;
            }
        }
    </style>
</head>

<body>
<div class="container right-panel-active">
    <!-- Sign Up -->
    <div class="container__form container--signup">
        <div class="form">
            <h2 class="form__title">Login</h2>
            <br/>
            <div class="input-group">
                <span class="input-group-addon">Username</span>
                <input type="text" class="form-control" placeholder="" id="username">
            </div>
            <br/>
            <div class="input-group">
                <span class="input-group-addon">Password</span>
                <input type="password" class="form-control" placeholder="" id="password">
            </div>
            <br/>
            <div class="input-group" style="display: flex;">
                <div style="width: 200px;height: 80px;text-align: center;">
                    <img src="" id="pictureCode" onclick="changePic();"> <!--verify--!>
                </div>
                <div>
                    <input type="text" class="form-control" placeholder="" id="text">
                </div>
                    <input type="text" class="form-control" id="uuid" style="display: none;">
            </div>
            <div style="display: flex;">
                <button class="btn btn-info .btn-large" style="width: 100px;margin-right: 20px;" id="validate">Login
                </button>
                <button class="btn btn-info .btn-info" style="width: 100px;margin-left: 20px;" id="register">Register
                </button>
            </div>
        </div>
    </div>


    <!-- Overlay -->
    <div class="container__overlay">
        <div class="overlay">
            <div class="overlay__panel overlay--left">
                <button class="btn" id="signIn" onclick="alert('Welcome to the world of Tech Titans!')">Tech Titans</button>
            </div>
            <div class="overlay__panel overlay--right">
                <button class="btn" id="signUp" onclick="alert('Welcome to the world of Tech Titans!')">Tech Titans</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        // changePic();
    });
    // 页面加载
    window.onload = function () {
        // 初始化内容
        changePic();
        console.log('changePic');
    }

    function createUuid() {
        var x = 1000;
        var y = 0;
        var random = parseInt(Math.random() * (x - y + 1) + y);
        $("#uuid").val(random);
    };

    // verify number
    function changePic() {
        createUuid();
        var uuid = $("#uuid").val();
        var verify = document.getElementById("pictureCode");
        verify.setAttribute('src', '${basePath}/rest/user/imageCode/' + uuid)
    }

    $("#register").click(function () {
        window.location.href = "${basePath}/register";
    });

    $("#validate").click(function () {
        jQuery.support.cors = true;
        var username = $("#username").val();
        var password = $("#password").val();
        var uuid = $("#uuid").val();
        var text = $("#text").val();
        let param = {
            "username": username,
            "password": password,
            "uid": uuid,
            "text": text
        }
        let obj = JSON.stringify(param);
        $.ajax({
            url: '${basePath}/rest/user/validate',//地址
            dataType: 'json',//数据类型
            type: 'POST',//类型
            timeout: 5000,//超时
            data: obj,
            contentType: "application/json",
            //请求成功
            success: function (data) {
                if (data.returnCode !== '200') {
                    alert(data.message);
                    return;
                }
                window.location.href = "${basePath}/region/list";
            },
            error: function (e) {
                console.log("e:" + e);
            }
        });
    });

</script>
</body>

</html>

