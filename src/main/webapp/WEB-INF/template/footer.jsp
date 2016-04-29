<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page
    import="com.surrey.com3014.group5.security.AuthoritiesConstants" %>
<div class="footer">
    <div class="container">
        <p class="text-muted"><i class="fa fa-university"></i> University of Surrey, COM3014 (Group-5) | Copyright @ 2016<sec:authorize access="hasAuthority('${AuthoritiesConstants.ADMIN}')"> | <a href="/swagger-ui/index.html">Swagger UI</a></sec:authorize></p>
    </div>
</div>
