<!DOCTYPE html>
<html class="no-js" lang="en">
    <head>
        <%@include file="../template/head.jsp" %>
    </head>
    <%@include file="../template/browserupgrade.jsp"%>
    <body>
        <div class="container">
            <h1 class="col-sm-offset-3 col-sm-6 well">Registration Form</h1>
            <div class="col-sm-offset-3 col-sm-6 well">
                <form action="/api/register" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <fieldset class="form-group">
                        <label for="username">Username</label>
                        <input type="text" class="form-control" id="username" name="username" placeholder="Enter username">
                    </fieldset>
                    <fieldset class="form-group">
                        <label for="password">Password</label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="Enter password">
                    </fieldset>
                    <fieldset class="form-group">
                        <label for="name">Name</label>
                        <input type="text" class="form-control" id="name" name="name" placeholder="Enter name">
                    </fieldset>
                    <fieldset class="form-group">
                        <label for="email">Email</label>
                        <input type="email" class="form-control" id="email" name="email" placeholder="Enter email">
                    </fieldset>
                    <button type="submit" class="btn btn-primary pull-right">Submit</button>
                </form>
            </div>
        </div>
        <%@include file="../template/scripts.jsp"%>
    </body>
</html>

