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
    <h1 class="col-sm-offset-3 col-sm-6 well">Update User</h1>
    <div class="col-sm-offset-3 col-sm-6 well form-padding-20-40">
        <form:form cssClass="form-horizontal" action="/admin/users/${managedUserDTO.id}"
                   method="post" modelAttribute="managedUserDTO" >

            <spring:bind path="username">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="control-label">Username</label>
                    <form:input path="username" cssClass="form-control " id="username"
                                placeholder="Username" readonly="true" value="${managedUserDTO.username}"/>
                    <form:errors path="username" cssClass="control-label"/>
                </div>
            </spring:bind>

            <spring:bind path="email">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="control-label">Email</label>
                    <form:input path="email" type="email" cssClass="form-control" id="email"
                                placeholder="Email" value="${managedUserDTO.email}"/>
                    <form:errors path="email" cssClass="control-label"/>
                </div>
            </spring:bind>

            <spring:bind path="name">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="control-label">Name</label>
                    <form:input path="name" cssClass="form-control " id="name"
                                placeholder="Name" value="${managedUserDTO.name}"/>
                    <form:errors path="name" cssClass="control-label"/>
                </div>
            </spring:bind>

            <spring:bind path="password">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="control-label">Password</label>
                    <form:password path="password" cssClass="form-control" id="password" placeholder="Password"/>
                    <form:errors path="password" cssClass="control-label"/>
                </div>
            </spring:bind>

            <spring:bind path="confirmPassword">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="control-label">Confirm Password</label>
                    <form:password path="confirmPassword" cssClass="form-control" id="password"
                                   placeholder="Confirm Password"/>
                    <form:errors path="confirmPassword" cssClass="control-label"/>
                </div>
            </spring:bind>

            <div class="form-group">
                <label class="control-label">Authority</label>
                <div>
                    <label class="radio-inline">
                        <input id="admin" name="admin" type="radio" value="true"
                               <c:if test="${managedUserDTO.admin == true}">checked="checked"</c:if>>Admin
                    </label>
                    <label class="radio-inline">
                        <input id="user" name="admin" type="radio" value="false"
                               <c:if test="${managedUserDTO.admin == false}">checked="checked"</c:if>>User
                    </label>
                </div>
            </div>

            <div class="form-group">
                <div class="checkbox">
                    <label><input id="enabled" name="enabled" type="checkbox" value="true"
                                  <c:if test="${managedUserDTO.enabled == true}">checked="checked"</c:if>> Enable</label>
                </div>
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-primary pull-right">Update</button>
            </div>
        </form:form>
    </div>
</div>
<%@include file="../../template/footer.jsp" %>
<%@include file="../../template/scripts.jsp" %>
</body>
</html>

