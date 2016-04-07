<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html class="no-js" lang="en">
<head>
    <%@include file="../../template/head.jsp" %>
</head>
<%@include file="../../template/browserupgrade.jsp" %>
<body>
<%@include file="../../template/navbar.jsp" %>
<div class="container">
    <h2 class="page-header">User List</h2>
    <div class="panel panel-default">
        <div class="panel-heading">
            <div class="row">
                <div class="btn-group pull-right">
                    <a href="/admin/users/new" role="button" class="btn btn-success btn-sm"><i
                        class="fa fa-user-plus"></i> Add New User</a>
                </div>
            </div>
        </div>
        <div class="panel-body">
            <table class="table table-striped table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Username</th>
                    <th>Created</th>
                    <th>Last Modified</th>
                    <th>Email</th>
                    <th>Name</th>
                    <th class="text-center">Admin</th>
                    <th class="text-center">Status</th>
                    <th class="text-right">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${users}" var="user">
                    <tr id="user-${user.id}">
                        <th scope="row">${user.id}</th>
                        <td>${user.username}</td>
                        <td><fmt:formatDate pattern="dd/MM/yyyy" value="${user.createdDate}"/></td>
                        <td><fmt:formatDate pattern="dd/MM/yyyy" value="${user.lastModifiedDate}"/></td>
                        <td>${user.email}</td>
                        <td>${user.name}</td>
                        <td class="text-center">
                            <c:choose>
                                <c:when test="${user.admin == true}">
                                    <i class="fa fa-check success"></i>
                                </c:when>
                                <c:otherwise>
                                    <i class="fa fa-times danger"></i>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-center">
                            <c:choose>
                                <c:when test="${user.enabled == true}">
                                    <i class="fa fa-unlock-alt success"></i>
                                </c:when>
                                <c:otherwise>
                                    <i class="fa fa-lock danger"></i>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-right">
                            <a href="#" class="btn btn-danger btn-xs btn-delete" data-target-id="${user.id}">
                                <i class="fa fa-trash"></i>
                            </a>
                            <a href="/admin/users/${user.id}" role="button" class="btn btn-warning btn-xs">
                                <i class="fa fa-pencil"></i>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../../template/modal.jsp">
    <jsp:param name="modal_id" value="delete-modal"/>
    <jsp:param name="title" value="Confirmation" />
    <jsp:param name="message" value="Are you sure you want to delete?" />
    <jsp:param name="btn_confirm_id" value="btn-delete-confirm" />
    <jsp:param name="btn_confirm_label" value="Delete" />
</jsp:include>

<div id="notification" class="alert alert-info alert-dismissible top-right notification" role="alert">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
        aria-hidden="true">&times;</span></button>
    <strong>Info!</strong> <span id="notification-message"></span>
</div>
<%@include file="../../template/footer.jsp" %>
<%@include file="../../template/scripts.jsp" %>
<script src="../../../assets/scripts/delete.js"></script>
</body>
</html>

