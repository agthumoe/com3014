<!DOCTYPE html>
<html class="no-js" lang="en">
<head>
    <%@include file="../template/head.jsp" %>
</head>
<%@include file="../template/browserupgrade.jsp" %>
<body>
<!-- Add your site or application content here -->
<jsp:include page="../template/navbar.jsp">
    <jsp:param name="active" value="game"/>
</jsp:include>
<div class="container">
    <div class="col-lg-12 col-md-12 col-sm-12" id="game-canvas">
    </div>
</div>
<%@include file="../template/footer.jsp" %>
<%@include file="../template/scripts.jsp" %>
    <script src="../../assets/libs/crafty.modified.js"></script>
    <script src="../../assets/libs/tron.js"></script>
    <script>
        $(function () {
            var game = Tron.instance("game-canvas");
            Tron.enterScene('LandingPage', game);
        });
    </script>
</body>
</html>
