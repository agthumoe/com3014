<!DOCTYPE html>
<html class="no-js" lang="en">
    <head>
        <%@include file="../template/head.jsp" %>
    </head>
    <%@include file="../template/browserupgrade.jsp"%>
    <body>
        <div class="container">
            <form action="/users/create" method="post">
                Username: <input type="text" name="username"><br>
                Password: <input type="password" name="password"><br>
                Name: <input type="text" name="name"><br>
                Email: <input type="email" name="email"><br>
                <input type="submit" value="Submit">
            </form>
        </div>
        <%@include file="../template/scripts.jsp"%>
    </body>
</html>

