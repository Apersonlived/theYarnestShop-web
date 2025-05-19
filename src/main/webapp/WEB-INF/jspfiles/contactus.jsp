<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Contact Us</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/contactus.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/header.css" />

<link href='https://fonts.googleapis.com/css?family=Leckerli One'
	rel='stylesheet'>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"
	rel="stylesheet" />
<style>
.contact-form {
	width: 50%;
	background-image:
		url("${pageContext.request.contextPath}/resources/image/contact.jpg");
	width: 50%;
	height: 100%;
	flex: left;
}
</style>
</head>
<body>
	<jsp:include page="header.jsp" />
	<main>
		<section class=contact>
			<section class=contact-form>
				<form action="${pageContext.request.contextPath}/Contact"
					method="POST" enctype="multipart/form-data"
					onsubmit="return validateForm(event)">
					<label for="fullname">Name </label> <input type="text"
						id="fullname" name="fullname" required pattern="[a-zA-Z ]+"
						title="Name can only contain letters" required> <label
						for="email">Email</label> <input type="email" id="email"
						name="email" required
						pattern="[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}+"
						title="Email must contain @---.com" required> <label
						for="message">Message</label> <input class="message" type="text"
						id="message" name="message">

					<button type="submit" class=message-submit>Submit</button>
				</form>
			</section>
			<section class=info>
				<div class=contact-header>
					<h3>Contact Us</h3>
				</div>
				<div class=contact-info>
				<h5>+977-9778689508</h5>
				<h5>theyarnestshop@gmail.com</h5>
				</div>
				<div class=media>
					<div class=media-img>
						<img
							src="${pageContext.request.contextPath}/resources/image/pinterestLogo1.png"
							alt="Pinterest">
					</div>
				</div>
			</section>
		</section>
	</main>
</body>
</html>