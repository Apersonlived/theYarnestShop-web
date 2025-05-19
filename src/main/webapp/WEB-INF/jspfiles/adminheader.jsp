<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="jakarta.servlet.http.HttpSession"%>
<%@ page import="jakarta.servlet.http.HttpServletRequest"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<div id=header>
	<header>
		<div class="header-top">
			<div class="logo">
				<h1>The Yarnest Shop</h1>
				<h6>Nest of Yarns</h6>
				<div class="auth-button">
					<c:choose>
						<c:when test="${not empty sessionScope.username}">
							<form action="${contextPath}/Logout" method="post">
								<input type="submit" value="Logout" />
							</form>
						</c:when>
						<c:otherwise>
							<form action="${contextPath}/Login" method="get">
								<input type="submit" value="Sign in" />
							</form>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<section class="sticky-header">
			<nav>
				<ul>
					<li><a href="${contextPath}/Dashboard">Dashboard</a></li>
					<li><a href="${contextPath}/ProductList">Products</a></li>
					<li><a href="${contextPath}/Users">Users</a></li>
				</ul>
			</nav>
			<div class=header-right>
				
					<div class=profile>
						<a href="${contextPath}/AdminProfile" class="profile-icon"> <i
							class="fa fa-user-circle" style='font-size: 36px;'></i>
						</a>
				</div>
			</div>
		</section>
	</header>
</div>