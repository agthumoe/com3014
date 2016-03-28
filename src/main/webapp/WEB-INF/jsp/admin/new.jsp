<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html class="no-js" lang="en">
<head>
    <%@include file="../../template/head.jsp" %>
</head>
<%@include file="../../template/browserupgrade.jsp" %>
<body>
<%@include file="../../template/navbar.jsp" %>
<div class="container">
    <h1 class="col-sm-offset-3 col-sm-6 well">Create New User</h1>
    <div class="col-sm-offset-3 col-sm-6 well form-padding-20-40">
        <form:form class="form-horizontal" method="post" modelAttribute="managedUserDTO" action="/admin/users">
            <spring:bind path="username">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="control-label">Username</label>
                    <form:input path="username" type="text" class="form-control " id="username" placeholder="Username"/>
                    <form:errors path="username" class="control-label"/>
                </div>
            </spring:bind>

            <spring:bind path="password">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="control-label">Password</label>
                    <form:password path="password" class="form-control" id="password" placeholder="Password"/>
                    <form:errors path="password" class="control-label"/>
                </div>
            </spring:bind>

            <spring:bind path="confirmPassword">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="control-label">Confirm Password</label>
                    <form:password path="confirmPassword" class="form-control" id="confirmPassword" placeholder="Confirm Password"/>
                    <form:errors path="confirmPassword" class="control-label"/>
                </div>
            </spring:bind>

            <spring:bind path="email">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="control-label">Email</label>
                    <form:input path="email" class="form-control" id="email" placeholder="Email"/>
                    <form:errors path="email" class="control-label"/>
                </div>
            </spring:bind>

            <spring:bind path="name">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="control-label">Name</label>
                    <form:input path="name" type="text" class="form-control " id="name" placeholder="Name"/>
                    <form:errors path="name" class="control-label"/>
                </div>
            </spring:bind>

            <spring:bind path="admin">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="control-label">Authority</label>
                    <div>
                        <label class="radio-inline">
                            <form:radiobutton path="admin" value="true"/>Admin
                        </label>
                        <label class="radio-inline">
                            <form:radiobutton path="admin" value="false"/>User
                        </label>
                        <form:errors path="admin" class="control-label"/>
                    </div>
                </div>
            </spring:bind>

            <spring:bind path="enabled">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <form:checkbox path="enabled" checked="checked" /> Enable
                </div>
            </spring:bind>
            <div class="form-group">
                <button type="submit" class="btn btn-success pull-right"><i class="fa fa-user-plus"></i> Create</button>
            </div>
        </form:form>
    </div>
</div>
<%@include file="../../template/footer.jsp" %>
<%@include file="../../template/scripts.jsp"%>
</body>
</html>
