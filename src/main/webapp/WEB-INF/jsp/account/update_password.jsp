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
    <h1 class="col-sm-offset-3 col-sm-6 well">Update Password</h1>
        <form:form cssClass="well form-horizontal col-md-offset-2 col-md-8 col-lg-offset-3 col-lg-6"
            action="/account/${id}/password" method="post" modelAttribute="updatePasswordDTO">
            <spring:bind path="currentPassword">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-4 control-label">Current Password</label>
                    <div class="col-sm-8">
                        <form:password path="currentPassword" cssClass="form-control" id="currentPassword"
                                    placeholder="Current Password" required="" autofocus=""/>
                        <form:errors path="currentPassword" cssClass="control-label" />
                    </div>
                </div>
            </spring:bind>

            <spring:bind path="password">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-4 control-label">New Password</label>
                    <div class="col-sm-8">
                        <form:password path="password" cssClass="form-control" id="password"
                                    placeholder="New Password" required="" />
                        <form:errors path="password" cssClass="control-label" />
                    </div>
                </div>
            </spring:bind>

            <spring:bind path="confirmPassword">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-4 control-label">Current Password</label>
                    <div class="col-sm-8">
                        <form:password path="confirmPassword" cssClass="form-control" id="confirmPassword"
                                    placeholder="Confirm Password" required="" />
                        <form:errors path="confirmPassword" cssClass="control-label" />
                    </div>
                </div>
            </spring:bind>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-primary pull-right">Update Password</button>
                </div>
            </div>
        </form:form>
</div>
<%@include file="../../template/footer.jsp" %>
<%@include file="../../template/scripts.jsp" %>
</body>
</html>

