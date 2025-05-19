<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Product List</title>
<link href="https://fonts.googleapis.com/css2?family=Inknut+Antiqua:wght@400;500;600;700&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/productlist.css">
</head>
<body class="bg-gray-100">
	<%@ include file="adminheader.jsp"%>

	<main class="product-page">
		<div class="product-list-header">
			<h2>Our Products</h2>
			<form method="get" action="${pageContext.request.contextPath}/ProductList" class="search-bar">
				<input type="text" name="query" placeholder="Search Products..." />
				<button type="submit">Search</button>
			</form>
		</div>

		<div class="action-header">
			<div></div>
			<a href="${pageContext.request.contextPath}/AddProduct" class="btn">Add New Product</a>
		</div>

		<c:if test="${not empty errorMessage}">
			<div class="error-message">
				<c:out value="${errorMessage}" />
			</div>
		</c:if>

		<div class="product-table-container">
			<table class="product-table">
				<thead>
					<tr>
						<th>ID</th>
						<th>Product Name</th>
						<th>Category</th>
						<th>Description</th>
						<th>Price</th>
						<th>Image</th>
						<th>Stock</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty productList}">
							<c:forEach var="product" items="${productList}">
								<tr>
									<td><c:out value="${product.product_id}" /></td>
									<td><c:out value="${product.product_name}" /></td>
									<td><c:out value="${product.category}" /></td>
									<td><c:out value="${product.description}" /></td>
									<td><c:out value="${product.price}" /></td>
									<td><c:choose>
											<c:when test="${not empty product.image}">
												<img src="${pageContext.request.contextPath}/${product.image}" alt="${product.product_name}" class="product-image" />
											</c:when>
											<c:otherwise>
                                                No Image
                                            </c:otherwise>
										</c:choose></td>
									<td><c:out value="${product.stock}" /></td>
									<td>
										<div class="action-buttons">
											<a href="${pageContext.request.contextPath}/EditProduct?productId=${product.product_id}" class="btn" title="Edit"> <i class="fas fa-edit"></i>Edit</a>
											<form action="${pageContext.request.contextPath}/ProductList" method="post" style="display: inline;">
												<input type="hidden" name="action" value="delete" /> <input type="hidden" name="productId" value="${product.product_id}" />
												<button type="submit" class="btn" title="Delete" onclick="return confirm('Are you sure you want to delete this product?');">
													<i class="fas fa-trash"></i>Delete
												</button>
											</form>
										</div>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="8">No products found or an error occurred.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>
	</main>
</body>
</html>