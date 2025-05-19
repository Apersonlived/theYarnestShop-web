<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add New Product</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/updateproduct.css">
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
	<%@ include file="adminheader.jsp" %>

    <main class="product-page">
        <div class="product-form-header">
            <h2>Add New Product</h2>
        </div>

        <div class="product-form-container">
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <c:out value="${errorMessage}" />
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/AddProduct" method="post" enctype="multipart/form-data" class="product-form">
                <div class="form-group">
                    <label for="productId">Product ID</label>
                    <input type="text" id="productId" name="productId" value="" required />
                </div>

                <div class="form-group">
                    <label for="productName">Product Name</label>
                    <input type="text" id="productName" name="productName" value="" required />
                </div>

                <div class="form-group">
                    <label for="category">Category</label>
                    <input type="text" id="category" name="category" value="" required />
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" rows="4" required></textarea>
                </div>

                <div class="form-group">
                    <label for="price">Price</label>
                    <input type="number" id="price" name="price" step="0.01" value="" required />
                </div>

                <div class="form-group">
                    <label for="imageFile">Image</label>
                    <input type="file" id="imageFile" name="imageFile" accept="image/*" />
                </div>

                <div class="form-group">
                    <label for="stock">Stock</label>
                    <input type="number" id="stock" name="stock" value="" required />
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/ProductList" class="btn btn-cancel">Cancel</a>
                    <button type="submit" class="btn">Save</button>
                </div>
            </form>
        </div>
    </main>
</body>
</html>