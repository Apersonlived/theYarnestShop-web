<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Product</title>
<link
	href="https://fonts.googleapis.com/css2?family=Inknut+Antiqua:wght@400;500;600;700&family=Inter:wght@400;500;600;700&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/updateproduct.css">
</head>
<body>
<body>
    <%@ include file="adminheader.jsp" %>

    <main class="product-page">
        <div class="product-form-header">
            <h2>Edit Product</h2>
        </div>

        <div class="product-form-container">
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <c:out value="${errorMessage}" />
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/EditProduct" method="post" enctype="multipart/form-data" class="product-form">
                <input type="hidden" name="productId" value="${product.product_id}" />
                <input type="hidden" name="existingImage" value="${product.image}" />

                <div class="form-group">
                    <label for="productId">Product ID</label>
                    <input type="text" id="productId" name="productId" value="${product.product_id}" readonly required />
                </div>

                <div class="form-group">
                    <label for="productName">Product Name</label>
                    <input type="text" id="productName" name="productName" value="${product.product_name}" required />
                </div>

                <div class="form-group">
                    <label for="category">Category</label>
                    <input type="text" id="category" name="category" value="${product.category}" required />
                </div>

                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea id="description" name="description" rows="4" required>${product.description}</textarea>
                </div>

                <div class="form-group">
                    <label for="price">Price</label>
                    <input type="number" id="price" name="price" step="0.01" value="${product.price}" required />
                </div>

                <div class="form-group">
                    <label for="imageFile">Image</label>
                    <input type="file" id="imageFile" name="imageFile" accept="image/*" />
                    <c:if test="${not empty product.image}">
                        <div>
                            <p>Current Image:</p>
                            <img src="${pageContext.request.contextPath}/${product.image}" alt="Current Product Image" class="product-image-preview" />
                        </div>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="stock">Stock</label>
                    <input type="number" id="stock" name="stock" value="${product.stock}" required />
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