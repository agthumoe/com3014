<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ page
	import="com.surrey.com3014.group5.security.AuthoritiesConstants"%>
<div class="header">
	<nav class="navbar navbar-default navbar-static-top" role="navigation">
		<div class="container">
			<div class="navbar-header">

				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#js-navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>

				<a class="navbar-brand" href="/">Tron</a>
			</div>

			<div class="collapse navbar-collapse" id="js-navbar-collapse">
				<ul class="nav navbar-nav">
					<c:choose>
						<c:when test="${param.active == 'game_info'}">
							<li class="active"><a href="#">Game Info</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="/">Game Info</a></li>
						</c:otherwise>
					</c:choose>
					<sec:authorize access="isAuthenticated()">
						<c:choose>
							<c:when test="${param.active == 'lobby'}">
								<li class="active"><a href="#">Lobby</a></li>
							</c:when>
							<c:otherwise>
								<li><a href="/lobby">Lobby</a></li>
							</c:otherwise>
						</c:choose>
					</sec:authorize>
					<c:choose>
						<c:when test="${param.active == 'help'}">
							<li class="active"><a href="#">Help</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="/help">Help</a></li>
						</c:otherwise>
					</c:choose>
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<sec:authorize access="isAnonymous()">
						<li><a href="/account/login"><i class="fa fa-sign-in"></i>
								Login</a></li>
						<li><a href="/account/register"><i
								class="fa fa-user-plus"></i> Register</a></li>
					</sec:authorize>
					<sec:authorize access="isAuthenticated()">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown" role="button" aria-haspopup="true"
							aria-expanded="false"><sec:authentication
									property="principal.username" /> <span class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a
									href="/account/<sec:authentication property="principal.id"/>/profile"><i
										class="fa fa-user"></i> Profile</a></li>
								<li><a
									href="/account/<sec:authentication property="principal.id"/>/password"><i
										class="fa fa-key"></i> Update Password</a></li>
								<sec:authorize
									access="hasAuthority('${AuthoritiesConstants.ADMIN}')">
									<li><a href="/admin/users"><i class="fa fa-tachometer"></i>
											User Management</a></li>
								</sec:authorize>
								<li role="separator" class="divider"></li>
								<li><a href="/logout"><i class="fa fa-sign-out"></i>
										Logout</a></li>
							</ul></li>
					</sec:authorize>
				</ul>
			</div>
		</div>
	</nav>
</div>
