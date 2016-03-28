<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!doctype html>
<html class="no-js" lang="en">
<head>
    <%@include file="../../template/head.jsp" %>
</head>
<%@include file="../../template/browserupgrade.jsp"%>
<body>
<%@include file="../../template/navbar.jsp" %>
<div class="container">
    <h1 class="col-sm-offset-3 col-sm-6 well">Registration Form</h1>
    <div class="col-sm-offset-3 col-sm-6 well">
        <form:form class="form-horizontal" method="post" modelAttribute="registerUserDTO" action="/account/register">

            <spring:bind path="username">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-4 control-label">Username</label>
                    <div class="col-sm-8">
                        <form:input path="username" type="text" class="form-control " id="username" placeholder="Username" />
                        <form:errors path="username" class="control-label" />
                    </div>
                </div>
            </spring:bind>

            <spring:bind path="password">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-4 control-label">Password</label>
                    <div class="col-sm-8">
                        <form:password path="password" class="form-control" id="password" placeholder="Password" />
                        <form:errors path="password" class="control-label" />
                    </div>
                </div>
            </spring:bind>

            <spring:bind path="confirmPassword">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-4 control-label">Confirm Password</label>
                    <div class="col-sm-8">
                        <form:password path="confirmPassword" class="form-control" id="confirmPassword" placeholder="Confirm Password" />
                        <form:errors path="confirmPassword" class="control-label" />
                    </div>
                </div>
            </spring:bind>

            <spring:bind path="email">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-4 control-label">Email</label>
                    <div class="col-sm-8">
                        <form:input path="email" class="form-control" id="email" placeholder="Email" />
                        <form:errors path="email" class="control-label" />
                    </div>
                </div>
            </spring:bind>

            <spring:bind path="name">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-4 control-label">Name</label>
                    <div class="col-sm-8">
                        <form:input path="name" type="text" class="form-control " id="name" placeholder="Name" />
                        <form:errors path="name" class="control-label" />
                    </div>
                </div>
            </spring:bind>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-primary pull-right">Register</button>
                </div>
            </div>
        </form:form>
    </div>
</div>
<%@include file="../../template/footer.jsp" %>
<%@include file="../../template/scripts.jsp"%>
</body>
</html>

