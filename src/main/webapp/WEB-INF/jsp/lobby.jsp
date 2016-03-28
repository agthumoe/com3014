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
    <div class="col-lg-8">
        <div class="row well">
            <table class="table">
                <caption><h2>Leader Board</h2></caption>
                <thead>
                <tr>
                    <th>#</th>
                    <th>Username</th>
                    <th>Win</th>
                    <th>Lost</th>
                    <th>Ratio</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th scope="row">1</th>
                    <td>Mark</td>
                    <td>10</td>
                    <td>1</td>
                    <td>10</td>
                </tr>
                <tr>
                    <th scope="row">2</th>
                    <td>Jacob</td>
                    <td>8</td>
                    <td>2</td>
                    <td>4</td>
                </tr>
                <tr>
                    <th scope="row">3</th>
                    <td>Larry</td>
                    <td>12</td>
                    <td>8</td>
                    <td>1.5</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="row well chat-box">
            <h2>Chat</h2>
            <div class="messages">
                <div class="message">
                    <span class="owner">Bob: </span><span class="message-body">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam sed metus vel purus molestie
                    dapibus condimentum iaculis mauris.</span><span class="time"> - [12/02/2016:13:30:48] </span>
                </div>
                <div class="message">
                    <span class="owner">Bob: </span><span class="message-body">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam sed metus vel purus molestie
                    dapibus condimentum iaculis mauris.</span><span class="time"> - [12/02/2016:13:30:48] </span>
                </div>
                <div class="message">
                    <span class="owner">Bob: </span><span class="message-body">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam sed metus vel purus molestie
                    dapibus condimentum iaculis mauris.</span><span class="time"> - [12/02/2016:13:30:48] </span>
                </div>
            </div><!-- ./messages -->
            <div class="message-input">
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="Type your message here...">
                      <span class="input-group-btn">
                        <button class="btn btn-primary" type="button">Send</button>
                      </span>
                </div><!-- /input-group -->
            </div>
        </div>

    </div>
    <div class="col-lg-4">
        <div class="well">
            <h2>Online Users</h2>
            <ul class="list-group">
                <li class="list-group-item"><i class="fa fa-circle status-online"></i> Mark <a
                    class="btn btn-xs btn-default pull-right" href="#" role="button"><i class="fa fa-bolt"></i></a></li>
                <li class="list-group-item"><i class="fa fa-circle status-online"></i> Jacob <a
                    class="btn btn-xs btn-default pull-right" href="#" role="button"><i class="fa fa-bolt"></i></a></li>
                <li class="list-group-item"><i class="fa fa-circle status-online"></i> Larry <a
                    class="btn btn-xs btn-default pull-right" href="#" role="button"><i class="fa fa-bolt"></i></a></li>
                <li class="list-group-item"><i class="fa fa-circle status-online"></i> Bob <a
                    class="btn btn-xs btn-default pull-right" href="#" role="button"><i class="fa fa-bolt"></i></a></li>
                <li class="list-group-item"><i class="fa fa-circle status-online"></i> Tom <a
                    class="btn btn-xs btn-default pull-right" href="#" role="button"><i class="fa fa-bolt"></i></a></li>
                <li class="list-group-item"><i class="fa fa-circle status-online"></i> Harry <a
                    class="btn btn-xs btn-default pull-right" href="#" role="button"><i class="fa fa-bolt"></i></a></li>
                <li class="list-group-item"><i class="fa fa-circle status-away"></i> James <a
                    class="btn btn-xs btn-default pull-right" href="#" role="button"><i class="fa fa-bolt"></i></a></li>
                <li class="list-group-item"><i class="fa fa-circle status-away"></i> Peter <a
                    class="btn btn-xs btn-default pull-right" href="#" role="button"><i class="fa fa-bolt"></i></a></li>
            </ul>
        </div>
    </div>
</div>
<%@include file="../template/footer.jsp" %>
<%@include file="../template/scripts.jsp" %>
</body>
</html>
