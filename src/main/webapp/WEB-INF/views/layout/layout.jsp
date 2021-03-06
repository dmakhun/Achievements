<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title><tiles:insertAttribute name="title" ignore="true"/></title>
    <style>
        @font-face {
            font-family: 'Glyphicons Halflings';
            src: url('<c:url value="/resources/fonts/" />glyphicons-halflings-regular.eot');
            src: url('<c:url value="/resources/fonts/" />glyphicons-halflings-regular.eot?#iefix') format('embedded-opentype'),
            url('<c:url value="/resources/fonts/" />glyphicons-halflings-regular.woff') format('woff'),
            url('<c:url value="/resources/fonts/" />glyphicons-halflings-regular.ttf') format('truetype'),
            url('<c:url value="/resources/fonts/" />glyphicons-halflings-regular.svg#glyphicons_halflingsregular') format('svg');
        }
    </style>
    <link href="<c:url value="/resources/css/menu.css" />" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="<c:url value="/resources/css/addAchiv.css" />" type="text/css"/>
    <link rel="stylesheet" href="<c:url value="/resources/css/custom.css" />" type="text/css"/>
    <link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui.css" />" type="text/css"/>
    <link rel="shortcut icon" href="<c:url value="/resources/favicon.ico" />" type="text/css"/>
    <script type="text/javascript" src="<c:url value="/resources/script/jquery.js" />"></script>
    <script src="<c:url value="/resources/script/bootstrap.js" />"></script>
</head>
<body>
<tiles:insertAttribute name="header"/>
<div class="container">
    <tiles:insertAttribute name="body"/>
    <tiles:insertAttribute name="footer"/>
</div>
</body>
</html>