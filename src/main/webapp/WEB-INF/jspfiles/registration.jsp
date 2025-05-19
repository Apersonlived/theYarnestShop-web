<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>Register page</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/registration.css" />
<link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
<style>
.registration-image {
	width: 944px;
	height: 1414px;
	background-image:
		url("${pageContext.request.contextPath}/resources/image/registerpage.jpg");
}
</style>
<script>
    function validateForm(event) {
    	const form = event.target;
        if (!form.checkValidity()) {
            event.preventDefault();
            return false;
        }
        
        const password = document.getElementById("password").value;
        const confirmPassword = document.getElementById("confirmpassword").value;
        
        if (password !== confirmPassword) {
            alert("Passwords do not match");
            event.preventDefault();
            return false;
        }
        
        return true;
    }
</script>

</head>
<body>
	<div class="registration-image">
		<div class="registration-box">

			<h2>Register With Us</h2>

			<form action="${pageContext.request.contextPath}/Register" method="POST" enctype="multipart/form-data" 
			onsubmit="return validateForm(event)">
			<h5 style='color:red; text-align: center'><jsp:include page="error.jsp" /></h5>

				<label for="fullname">Full Name </label> <input type="text"
					id="fullname" name="fullname" required pattern="[a-zA-Z ]+" title="Name can only contain letters" required> 
					<label for="username">User Name</label>
                <input type="text" id="username" name="username" required pattern="[a-zA-Z0-9_]+" 
                title="Username can only contain letters, numbers, and underscores">

                <label for="email">Email</label>
                <input type="email" id="email" name="email" required pattern="[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}+" 
                title="Email must contain ---@---.com" required>

                <label for="phone">Phone Number</label>
                <input type="tel" id="phone" name="phone" required pattern="[0-9]{10}" title="Phone number must be 10 digits">

                <label for="address">Address</label>
                <input type="text" id="address" name="address" required pattern="[a-zA-Z0-9 ]+" 
                title="Address can only contain letters, spaces and numbers" required>

                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
				<label for="confirmpassword">Confirm Password</label> <input
					type="password" id="confirmpassword" name="confirmpassword"
					required>
				<label for="myfile">Profile Picture</label> 
				<input type="file" id="myfile" name="myfile">
					<h5 align = center><a href="${pageContext.request.contextPath}/Login">Already have an account? Log-in!</a></h5>

				<button type="submit" class="registration-button">Sign Up</button>

			</form>
			
		</div>
	</div>
</body>
</html>