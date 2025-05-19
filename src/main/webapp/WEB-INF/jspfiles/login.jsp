<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Log-in Page</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/log-in.css" />
<link
	href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap"
	rel="stylesheet">
<style>
.login-image {
	width: 1200px;
	height: 700px;
	margin: 50px;
	display: flex;
	background-image:
		url("${pageContext.request.contextPath}/resources/image/log-in.jpg");
	opacity: 0.7;
}
</style>
</head>
<body>
	<div class=login-image>
		<div class=login-box>
			<h2>Welcome to The Yarnest Shop</h2>
			<form action="${pageContext.request.contextPath}/Login" method="POST">
				<h5 style='color: red; text-align: center'><jsp:include
						page="error.jsp" /></h5>
				<label for="username">User Name </label> <input type="text"
					id="username" name="username" required> <label
					for="password">Password</label> <input type="password"
					id="password" name="password" required>

				<button type="submit" class="login-button">Log-In</button>
			</form>
			<h5 align=center>
				<a href="${pageContext.request.contextPath}/Register">Sign up to
					create a new account!!!</a>
			</h5>
		</div>
	</div>

</body>
</html>