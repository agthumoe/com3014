<!DOCTYPE html>
<html class="no-js" lang="en">
<head>
    <%@include file="../template/head.jsp" %>
</head>
<%@include file="../template/browserupgrade.jsp" %>
<body>
<!-- Add your site or application content here -->
<jsp:include page="../template/navbar.jsp">
    <jsp:param name="active" value="lobby"/>
</jsp:include>
<div class="container">
    <div class="col-lg-8 col-md-8">
        <div class="row well">
            <table class="table" id="leaderboard-table">
                <caption><h2>Leader Board</h2></caption>
                <thead>
                <tr>
                    <th>Username</th>
                    <th>Win</th>
                    <th>Lost</th>
                    <th>Ratio</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
        <div class="row well chat-box">

            <h2>Chat</h2>
            <div class="message-input">
                <div class="form-group">
                    <input id="chat-input" type="text" class="form-control" placeholder="Type your message here...">
                </div><!-- /input-group -->
            </div>
            <div id="messages" class="messages">
            </div><!-- ./messages -->

        </div>

    </div>
    <div class="col-lg-4 col-md-4">
        <div class="well">
            <h2>Online Users</h2>
            <ul id="online-users" class="list-group">
                <!--<li class="list-group-item"><i class="fa fa-circle status-online"></i> Mark <a
                    class="btn btn-xs btn-default pull-right" href="#" role="button"><i class="fa fa-bolt"></i></a></li>-->

            </ul>
        </div>
    </div>
</div>
<%@include file="../template/footer.jsp" %>
<%@include file="../template/scripts.jsp" %>
<script src="../../bower_components/sockjs/sockjs.min.js"></script>
<script src="../../bower_components/stomp-websocket/lib/stomp.min.js"></script>
<script src="../../assets/libs/tron.chat.js"></script>
<script src="../../assets/libs/tron.active-users.js"></script>
<script type="text/javascript">
    $(function () {
        var chat = TronChat.create();
        chat.init('#chat-input', '#messages');
        chat.setMessagesMaxHeight(500);

        var activeUsers = TronActiveUsers.create();
        activeUsers.init("#online-users");
    });
</script>
<script src="../../assets/scripts/leaderboard.js"></script>
</body>
</html>
