<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tron | 500</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="../../assets/styles/error_page.css">
</head>
<body>
<div class="wrap">
    <div class="banner">
        <img src="../../assets/images/error/computer-reaction-meme.png" alt=""/>
    </div>
    <div class="page">
        <h1>Ermmm...</h1>
        <h2>That doesn't look quite right!</h2>
        <h3>Don't freak out! Please <a href="/">go back</a> and relax! We will solve the issue ASAP!</h3>
    </div>
    <!--
    Failed URL: ${url}
    Exception:  ${exception.message}
        <c:forEach items="${exception.stackTrace}" var="ste">    ${ste}
    </c:forEach>
    -->
</div>
</body>
</html>
