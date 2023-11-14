
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>登录页</title>
</head>
<body>
    <p>${error}</p>
    <form action="/login">
        <tr>
            <td>账号：</td>
            <td>
                <input type="text" name="username" value="admin">
            </td>
        </tr>
        <br/>
        <tr>
            <td>密码：</td>
            <td>
                <input type="password" name="password" value="123456">
            </td>
        </tr>
        <input type="submit" value="登录">
    </form>
</body>
</html>
