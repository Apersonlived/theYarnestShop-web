<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Landing Page</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/landing.css" />
<link href="https://fonts.googleapis.com/css2?family=Joan&display=swap"
	rel="stylesheet">


</head>
<body>
	<h6>@Copyright The Yarnest Shop 2025</h6>
	<h2>The Yarnest Shop</h2>
	<h3>Nest of Yarns</h3>
	<div class=info-box>
		<p>Need Gifts for your loved ones? This season gift them with the
			cute goodies crocheted with love!</p>
	</div>

	<button type="submit" class="entry-button">Shop With Us</button>

	<div class=media>
	<div class=media-img>
	<a href=url("${pageContext.request.contextPath}/resources/image/registerpage.jpg")></a>
	</div>
	</div>
</body>
</html>