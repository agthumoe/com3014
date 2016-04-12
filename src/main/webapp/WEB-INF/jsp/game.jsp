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
<script src="../../bower_components/sockjs/sockjs.min.js"></script>
<script src="../../bower_components/stomp-websocket/lib/stomp.min.js"></script>
<script type="text/javascript" src="../../assets/libs/tron.game.js"></script>
<script type="text/javascript" src="../../assets/libs/tron.js"></script>
<script type="text/javascript">
    $(function () {
        window.User = null;
        $.get({
            url: '/api/account',
            success: function (response) {
                window.User = response;
                
                Tron('pre-game', function (m) {
                    m.init();
                    m.initGame(400, 800, 'challenger');
                });
            },
            error: function () {
                console.log("Couldn't get active user");
            }
        });
    });
</script>
</body>
</html>
