<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>The Yarnest Shop - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inknut+Antiqua:wght@400;700&display=swap" rel="stylesheet">
</head>
<body>
    <%@ include file="header.jsp" %>
    
    <main>
        <section class="welcome">
            <div class="welcome-content">
                <h2>Cute crochet items for your gifts!</h2>
                <p>Gift you and your loved ones with hand made goodies. Elevate your boring stuff with cute crochet.</p>
                <a href="${pageContext.request.contextPath}/Products" class="welcome-btn">Shop Now</a>
            </div>
        </section>
        
        <section class="best-sellers">
            <h2>Our Best Sellers</h2>
            <div class="products-grid">
                <!-- Products will be dynamically inserted here -->
            </div>
        </section>
        
        <section class="about">
            <h2><a href="${pageContext.request.contextPath}/About" class="about-btn">About Us</a></h2>
            <p>We source our yarn from ethical local businesses and are committed to creating, producing and selling ethically sourced products. Our pledge to produce long lasting and ethically sound products are a strong commitment to our customers.</p>
        </section>
    </main>
    
    <%@ include file="footer.jsp" %>
    
    <script>
        function searchProducts() {
            const searchTerm = document.getElementById('searchInput').value;
            window.location.href = '${pageContext.request.contextPath}/Products?search=' + searchTerm;
        }
    </script>
</body>
</html>