<%@include file="../template/header.jsp" %>
<div class="container">
    <form action="/users/create" method="post">
        Username: <input type="text" name="username"><br>
        Password: <input type="password" name="password"><br>
        Name: <input type="text" name="name"><br>
        Email: <input type="email" name="email"><br>
        <input type="submit" value="Submit">
    </form>
</div>
<%@include file="../template/footer.jsp" %>
