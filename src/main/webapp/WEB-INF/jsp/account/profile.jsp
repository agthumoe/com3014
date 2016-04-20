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
    <h1 class="page-header">User Profile</h1>
    <div class="row">
        <div class="col-sm-offset-1 col-sm-2 text-left">
            <span class="fa-stack fa-lg icon-profile">
                <i class="fa fa-circle fa-stack-2x"></i>
                <i class="fa fa-user fa-stack-1x"></i>
            </span>
        </div>
        <div class="col-sm-4">
            <ul class="list">
                <li class="list-item"><strong>Username: </strong>${userDTO.username}</li>
                <li class="list-item"><strong>Email: </strong>${userDTO.email}</li>
                <li class="list-item"><strong>Name: </strong>${userDTO.name}</li>
                <li class="list-item">
                    <a href="/account/${userDTO.id}" class="btn btn-info">
                        <i class="fa fa-pencil-square-o" aria-hidden="true"></i> Update
                    </a>
                </li>
            </ul>
        </div>
        <div class="col-sm-4">

        </div>
    </div>
    <hr>
    <div class="row">
        <div class="col-xs-6 col-md-4 col-lg-4 text-center">
            <div class="x-panel info border-right-2px">
                <div class="x-panel-heading">Wins <i class="fa fa-thumbs-up" aria-hidden="true"></i></div>
                <span class="badge">${userDTO.wins}</span>
            </div>
        </div>

        <div class="col-xs-6 col-md-4 col-lg-4 text-center">
            <div class="x-panel success border-right-2px">
                <div class="x-panel-heading">Rating <i class="fa fa-trophy" aria-hidden="true"></i></div>
                <span class="badge">${userDTO.rating}</span>
            </div>
        </div>
        <div class="col-xs-6 col-md-4 col-lg-4 text-center">
            <div class="x-panel danger">
                <div class="x-panel-heading">Loses <i class="fa fa-thumbs-down" aria-hidden="true"></i></div>
                <span class="badge">${userDTO.losses}</span>
            </div>
        </div>
    </div>
</div>
<%@include file="../../template/footer.jsp" %>
<%@include file="../../template/scripts.jsp" %>
</body>
</html>

