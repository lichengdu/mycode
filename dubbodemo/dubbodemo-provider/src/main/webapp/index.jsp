<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dubbox Test Page</title>
    <link href="<c:url value='/lib/bootstrap/css/bootstrap.min.css'/>" rel="stylesheet">

    <style type="text/css">

        .floatSet {
            text-align: center;
            line-height: 50px;
        }

        .floatSet div {
            float: left;
            margin-left: 5px;
        }
    </style>
</head>
<body>
<!-- 顶部导航 -->
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation" id="menu-nav">

</div>

<div class="container floatSet" style="margin-top: 80px;">
     Dubbox start OK! Welcome!
</div>
<script src="<c:url value='/lib/jquery-1.8.3.min.js'/>"></script>
<script src="<c:url value='/lib/bootstrap/js/bootstrap.min.js'/>"></script>
</body>
</html>