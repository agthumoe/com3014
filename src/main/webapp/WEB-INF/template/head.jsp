<%--
  This file contains all necessary tags inside html <head> tag including css files and modernizr script file.
  Any other css files should be included after including this file.

  Author: Aung Thu Moe
  Date: 05/03/2016
  Time: 16:18
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<c:choose>
    <c:when test="${empty title}">
        <title>Tron Game | Group 5</title>
    </c:when>
    <c:otherwise>
        <title>${title}</title>
    </c:otherwise>
</c:choose>
<c:if test="${!empty description}">
    <meta name="description" content="${description}">
</c:if>

<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="../../bower_components/bootstrap/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="../../assets/styles/main.css">
<script src="../../bower_components/modernizr-built/dist/modernizr.min.js"></script>

