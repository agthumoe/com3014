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
<jsp:include page="../template/modal.jsp">
    <jsp:param name="modal_id" value="challenge-modal"/>
    <jsp:param name="title" value="New Challenge"/>
    <jsp:param name="message" value="You receive new game challenge"/>
    <jsp:param name="btn_yes_id" value="btn-challenge-accept"/>
    <jsp:param name="btn_yes_label" value="Accept"/>
    <jsp:param name="btn_no_id" value="btn-challenge-deny"/>
    <jsp:param name="btn_no_label" value="Deny"/>
</jsp:include>
<%@include file="../template/footer.jsp" %>
<%@include file="../template/scripts.jsp" %>
<script src="../../bower_components/sockjs/sockjs.min.js"></script>
<script src="../../bower_components/stomp-websocket/lib/stomp.min.js"></script>
<script src="../../assets/libs/tron.chat.js"></script>
<script src="../../assets/libs/tron.challenge.js"></script>
<script src="../../assets/libs/tron.active-users.js"></script>
<script src="../../assets/libs/tron.js"></script>
<script type="text/javascript">
    $(function () {
        window.User = null;
        $.get({
            url: '/api/account',
            success: function (response) {
                window.User = response;
                
                var challengeManager = Tron('challenge', function (m) {
                    m.init(User.username);
                });

                Tron('chat', function (m) {
                    m.init('#chat-input', '#messages');
                    m.setMessagesMaxHeight(500);
                });

                Tron('active-users', function (m) {
                    m.init("#online-users", function (e) {
                        challengeManager.newChallenge(e.data.userID);
                    });
                });
            },
            error: function () {
                console.log("Couldn't get active user");
            }
        });
    });
</script>
<script src="../../assets/scripts/leaderboard.js"></script>
</body>
</html>
