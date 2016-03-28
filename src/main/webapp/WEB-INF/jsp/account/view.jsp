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
    <h1 class="col-sm-offset-3 col-sm-6 well">User Info</h1>
    <div class="col-sm-offset-3 col-sm-6 well">
        <form:form class="form-horizontal" method="post" modelAttribute="updateUserDTO" action="/account/${userDTO.id}">

            <spring:bind path="username">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-4 control-label">Username</label>
                    <div class="col-sm-8">
                        <form:input path="username" type="text" class="form-control " id="username"
                                    placeholder="Username" readonly="true" value="${userDTO.username}"/>
                        <form:errors path="username" class="control-label" />
                    </div>
                </div>
            </spring:bind>

            <spring:bind path="email">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-4 control-label">Email</label>
                    <div class="col-sm-8">
                        <form:input path="email" type="email" class="form-control" id="email"
                                    placeholder="Email" value="${userDTO.email}"/>
                        <form:errors path="email" class="control-label" />
                    </div>
                </div>
            </spring:bind>

            <spring:bind path="name">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-4 control-label">Name</label>
                    <div class="col-sm-8">
                        <form:input path="name" type="text" class="form-control " id="name"
                                    placeholder="Name" value="${userDTO.name}"/>
                        <form:errors path="name" class="control-label" />
                    </div>
                </div>
            </spring:bind>

            <spring:bind path="currentPassword">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-4 control-label">Password</label>
                    <div class="col-sm-8">
                        <form:password path="currentPassword" class="form-control" id="currentPassword" placeholder="Please enter currentPassword to confirm update" />
                        <form:errors path="currentPassword" class="control-label" />
                    </div>
                </div>
            </spring:bind>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-primary pull-right">Update</button>
                </div>
            </div>
        </form:form>
    </div>
</div>
<%@include file="../../template/footer.jsp" %>
<%@include file="../../template/scripts.jsp"%>
</body>
</html>

