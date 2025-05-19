<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="adminheader.jsp" />
    <main>
        <c:if test="${not empty error}">
            <p style="color: red;">${error}</p>
        </c:if>
        <c:if test="${not empty message}">
            <p style="color: green;">${message}</p>
        </c:if>
        <form class="profile-form" action="${pageContext.request.contextPath}/Profile" method="POST" enctype="multipart/form-data">
            <div class="photo-side">
                <div class="user-img">
                    <img src="${pageContext.request.contextPath}/resources/image/user/${user.image_path}" 
             alt="Profile Image" 
             onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/resources/image/user/default.jpg';">
                        
                </div>
                <div class="form-group">
                    <label for="email">Email</label> 
                    <input type="email" id="email" name="email" value="${user.email}" required>
                </div>
                <div class="form-group">
                    <label for="phoneNumber">Phone Number</label> 
                    <input type="tel" id="phoneNumber" name="phoneNumber" value="${user.phone}" pattern="[0-9]{10}" required>
                </div>
                <div class="form-group">
                    <label for="address">Address</label> 
                    <input type="text" id="address" name="address" value="${user.address}" required>
                </div>
            </div>
            <div class="form-side">
                <h4>${user.full_name}</h4>
                <h5>ID: ${user.user_id}</h5>
                <div class="form-group">
                    <label for="username">User Name</label> 
                    <input type="text" id="username" name="username" value="${user.user_name}" required>
                </div>
                <div class="form-group">
                    <label for="myfile">Profile Picture</label> 
                    <input type="file" id="myfile" name="myfile" accept="image/*">
                </div>
                <input type="hidden" name="userId" value="${user.user_id}">
                <input type="hidden" name="fullName" value="${user.full_name}">
                <div class="form-button">
                    <button type="submit" class="submit-btn">Update Profile</button>
                    <button type="button" class="submit-btn" onclick="deleteAccount()">Delete Account</button>
                </div>
            </div>
        </form>
    </main>
    
    <script>
        document.querySelector('.profile-form').addEventListener('submit', function(e) {
            const phone = document.getElementById('phoneNumber').value;
            if (!/^\d{10}$/.test(phone)) {
                e.preventDefault();
                alert('Please enter a valid 10-digit phone number');
                return false;
            }
            return true;
        });

        function deleteAccount() {
            if (confirm('Are you sure you want to delete your account?')) {
                fetch('${pageContext.request.contextPath}/Profile', {
                    method: 'DELETE'
                }).then(response => {
                    if (response.ok) {
                        window.location.href = '${pageContext.request.contextPath}/logout.jsp';
                    } else {
                        alert('Error deleting account');
                    }
                }).catch(error => {
                    alert('Error: ' + error.message);
                });
            }
        }
    </script>
</body>
</html>