<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html class="no-js" lang="en">
<head>
    <%@include file="../../template/head.jsp" %>
</head>
<%@include file="../../template/browserupgrade.jsp" %>
<body>
<%@include file="../../template/navbar.jsp" %>
<div class="container">
    <%--<h2 class="col-lg-offset-3 col-lg-6 col-md-offset-2 col-md-8 col-sm-12 well">Please login to continue...</h2>--%>
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible top-right notification" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
            <strong>Warning!</strong> ${error}
        </div>
    </c:if>
    <c:if test="${not empty msg}">
        <div class="alert alert-info alert-dismissible top-right notification" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
            <strong>Info!</strong> ${msg}
        </div>
    </c:if>
    <form class="form-login well" method="post" action="/api/login">
        <h2>Please Login</h2>
        <input type="text" class="form-control" id="username" name="username" placeholder="Username" required/>
        <input type="password" class="form-control" id="currentPassword" name="currentPassword" placeholder="Password" required/>
        <button type="submit" class="btn btn-lg btn-primary btn-block">Login</button>
    </form>
</div>
<%@include file="../../template/footer.jsp" %>
<%@include file="../../template/scripts.jsp" %>
</body>
</html>

