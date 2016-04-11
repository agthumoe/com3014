<jsp:useBean id="gameID" scope="request" type="java.lang.String"/>
<!DOCTYPE html>
<html class="no-js" lang="en">
<head>
    <%@include file="../template/head.jsp" %>
</head>
<%@include file="../template/browserupgrade.jsp" %>
<body>
<span data-game-id="${gameID}"></span>
<%@include file="../template/scripts.jsp" %>
<script type="text/javascript" src="../../bower_components/jquery.cookie/jquery.cookie.js"></script>
<script type="text/javascript" src="../../assets/libs/crafty.modified.js"></script>
<script type="text/javascript" src="../../assets/libs/tron.game.js"></script>
</body>
</html>
