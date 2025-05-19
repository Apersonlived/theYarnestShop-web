<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User List</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/users.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <%@ include file="adminheader.jsp" %>
    
    <main class="user-list-container">
        <div class="user-list-header">
            <h2>Our Users</h2>
        </div>
        
        <div class="user-table-container">
            <table class="user-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Address</th>
                        <th>Profile</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty users}">
                            <c:forEach var="user" items="${users}">
                                <tr>
                                    <td><c:out value="${user.user_id}"/></td>
                                    <td><c:out value="${user.full_name}"/></td>
                                    <td><c:out value="${user.user_name}"/></td>
                                    <td><c:out value="${user.email}"/></td>
                                    <td><c:out value="${user.phone}"/></td>
                                    <td><c:out value="${user.address}"/></td>
                                    <td><c:out value="${user.image_path}"/></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6">No users found</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </main>
    
    <script>
        function searchProducts() {
            const searchTerm = document.getElementById('searchInput').value;
            window.location.href = '${pageContext.request.contextPath}/Products?search=' + encodeURIComponent(searchTerm);
        }
    </script>
</body>
</html>