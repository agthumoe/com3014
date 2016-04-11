<!DOCTYPE html>
<html class="no-js" lang="en">
<head>
    <%@include file="../template/head.jsp" %>
</head>
<%@include file="../template/browserupgrade.jsp" %>
<body>
<jsp:include page="../template/navbar.jsp">
    <jsp:param name="active" value="game"/>
</jsp:include>
<div class="container">
    <div id="game-canvas"></div>
</div>
<%@include file="../template/footer.jsp" %>
<%@include file="../template/scripts.jsp" %>
<script type="text/javascript" src="../../bower_components/jquery.cookie/jquery.cookie.js"></script>
<script type="text/javascript" src="../../assets/libs/crafty.modified.js"></script>
<script type="text/javascript" src="../../assets/libs/tron.game.js"></script>
<script type="text/javascript">
    $(function () {
        var tron = TronGame.instance();
        tron.init('game-canvas');
        tron.enterScene('LandingPage', tron);
    });
</script>
</body>
</html>
