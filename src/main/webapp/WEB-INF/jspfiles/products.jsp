<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>The Yarnest Shop - Products</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/header.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/products.css">
</head>
<body>
	<jsp:include page="header.jsp" />

	<c:if test="${sessionScope.orderSuccess}">
		<script>alert("✅ Your order has been successfully placed!");</script>
		<c:remove var="orderSuccess" scope="session" />
	</c:if>

	<c:if test="${not empty sessionScope.errorMessage}">
		<script>alert("${sessionScope.errorMessage}");</script>
		<c:remove var="errorMessage" scope="session" />
	</c:if>

	<div class="container">
		<header class="products-header">
			<h1>Explore Our Crochets</h1>
			<form method="get"
				action="${pageContext.request.contextPath}/Products"
				class="search-bar">
				<input type="text" name="query" placeholder="Search Products..." />
				<button type="submit">Search</button>
			</form>

			<form method="get"
				action="${pageContext.request.contextPath}/Products"
				class="filter-bar">
				<select name="category" class="category-filter">
					<option value="">All Categories</option>
					<option value="Yarn" ${param.category == 'Yarn' ? 'selected' : ''}>Yarn</option>
					<option value="Bookmark"
						${param.category == 'Bookmark' ? 'selected' : ''}>Bookmark</option>
					<option value="Keyring"
						${param.category == 'Keyring' ? 'selected' : ''}>Keyring</option>
					<option value="Clips"
						${param.category == 'Clips' ? 'selected' : ''}>Clips</option>
				</select> <input type="hidden" name="query" value="${param.query}" />
				<button type="submit">Filter</button>
			</form>
		</header>

		<section class="product-grid">
			<c:choose>
				<c:when test="${not empty productList}">
					<c:forEach var="product" items="${productList}">
						<div class="product-card">
							<div class="product-image">
								<c:choose>
									<c:when test="${not empty product.image}">
										<img src="${pageContext.request.contextPath}/${product.image}"
											alt="${product.product_name}" />
									</c:when>
									<c:otherwise>
										<div class="no-image">No Image Available</div>
									</c:otherwise>
								</c:choose>
							</div>
							<div class="product-details">
								<h3>${product.product_name}</h3>
								<p class="category">${product.category}</p>
								<p class="description">${product.description}</p>
								<p class="price">
									<fmt:formatNumber value="${product.price}" type="currency" />
								</p>
								<form method="post"
									action="${pageContext.request.contextPath}/Order">
									<input type="hidden" name="productId"
										value="${product.product_id}" /> <input type="hidden"
										name="productPrice" value="${product.price}" />
									<div class="quantity-container">
										<label for="quantity_${product.product_id}">Quantity:</label>
										<input type="number" id="quantity_${product.product_id}"
											name="quantity" value="1" min="1" max="100" required />
									</div>
									<button type="submit" class="buy-button"
										onclick="return confirmOrder('${product.product_id}', '${product.product_name}', 
										<fmt:formatNumber value="${product.price}" type="number" groupingUsed="false" />)">Buy</button>
								</form>
							</div>
						</div>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="no-products">
						<p>No products found. Please try a different category or
							search term.</p>
					</div>
				</c:otherwise>
			</c:choose>
		</section>
	</div>

	<script>
	function confirmOrder(productId, productName, price) {
	    const quantity = document.getElementById('quantity_' + productId).value;
	    if (quantity <= 0) {
	        alert("Please select a valid quantity");
	        return false;
	    }

	    const totalPrice = price * quantity;
	    const formattedPrice = new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(totalPrice);

	    return confirm(`Are you sure you want to order ${quantity} ${productName}(s) for ${formattedPrice}?`);
	}
	</script>
</body>
</html>