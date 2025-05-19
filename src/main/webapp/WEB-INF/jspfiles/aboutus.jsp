<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>About Us</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/aboutus.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/header.css" />		
	
<link
	href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap"
	rel="stylesheet">
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
	rel="stylesheet" />
</head>
<body>
<jsp:include page="header.jsp" />
<main>
<div class= about>
<h3>About Us</h3>
<div class=founder>
<div class=founder-img>
	<img src="${pageContext.request.contextPath}/resources/image/about.jpg" alt = "Founder">
</div>
<div class=founder-img>
	<img src="${pageContext.request.contextPath}/resources/image/about.jpg" alt = "Founder">
</div>
<div class=founder-img>
	<img src="${pageContext.request.contextPath}/resources/image/about.jpg" alt = "Founder">
</div>
<div class=founder-img>
	<img src="${pageContext.request.contextPath}/resources/image/about.jpg" alt = "Founder">
</div>
</div>
<p>We are a team of passionate individuals focused on sustainable means of production. 
With the love for crochet our team is dedicated to creating memorable moments between you
 and your loved ones with products that could last for a long time.</p>
</div>
</main>
</body>
</html>