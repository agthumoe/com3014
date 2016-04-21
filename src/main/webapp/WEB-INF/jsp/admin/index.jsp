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
    <div class="row">
        <div class="col-xs-6 col-sm-3 col-md-3 col-lg-3 border-right-2px">
            <div class="x-panel info text-center">
                <div class="x-panel-heading">Total Users</div>
                <span class="badge-large">${totalUsers}</span>
            </div>
        </div>
        <div class="col-xs-6 col-sm-3 col-md-3 col-lg-3 border-right-2px">
            <div class="x-panel success text-center">
                <div class="x-panel-heading">Active Users</div>
                <span class="badge-large">${activeUsers}</span>
            </div>
        </div>
        <div class="col-xs-6 col-sm-3 col-md-3 col-lg-3 border-right-2px">
            <div class="x-panel danger text-center">
                <div class="x-panel-heading">Banned Users</div>
                <span class="badge-large">${bannedUsers}</span>
            </div>
        </div>
        <div class="col-xs-6 col-sm-3 col-md-3 col-lg-3">
            <div class="x-panel warning text-center">
                <div class="x-panel-heading">Admins</div>
                <span class="badge-large">${numberOfAdmins}</span>
            </div>
        </div>
    </div>
    <hr>
    <%@include file="../../template/filter.jsp" %>
    <div class="row">
        <table id="user-table" class="table table-striped table-bordered table-hover">
            <thead>
            <tr>
                <th><a href="#">ID <i class="fa fa-sort"></i></a></th>
                <th><a href="#">Username <i class="fa fa-sort"></i></a></th>
                <th><a href="#">Created <i class="fa fa-sort"></i></a></th>
                <th><a href="#">Last Modified <i class="fa fa-sort"></i></a></th>
                <th><a href="#">Email <i class="fa fa-sort"></i></a></th>
                <th><a href="#">Name <i class="fa fa-sort"></i></a></th>
                <th class="text-center"><a href="#">Status <i class="fa fa-sort"></i></a></th>
                <th class="text-center">Role</th>
                <th class="text-right"></th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
    <div class="row">
        <nav class="centre">
            <ul class="pagination pagination-sm" id="pagination">
            </ul>
        </nav>
    </div>
</div>

<jsp:include page="../../template/modal.jsp">
    <jsp:param name="modal_id" value="delete-modal"/>
    <jsp:param name="title" value="Confirmation"/>
    <jsp:param name="message" value="Are you sure you want to delete?"/>
    <jsp:param name="btn_yes_id" value="btn-delete-confirm"/>
    <jsp:param name="btn_yes_label" value="Delete"/>
    <jsp:param name="btn_no_id" value="btn-close"/>
    <jsp:param name="btn_no_label" value="Close"/>
</jsp:include>

<div id="notification" class="alert alert-info alert-dismissible top-right notification" role="alert">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
        aria-hidden="true">&times;</span></button>
    <strong>Info!</strong> <span id="notification-message"></span>
</div>
<%@include file="../../template/footer.jsp" %>
<%@include file="../../template/scripts.jsp" %>
<script src="../../../bower_components/jQuery-Pagination-Plugin/js/jquery.yzePagination.min.js"></script>
<script src="../../../assets/scripts/users.js"></script>
</body>
</html>

