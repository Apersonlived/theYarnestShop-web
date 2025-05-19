package com.theYarnestShop.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

import com.theYarnestShop.dao.ProductDAO;
import com.theYarnestShop.model.ProductModel;
import com.theYarnestShop.util.ImageUtil;
import com.theYarnestShop.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * Controller for adding new products.
 */
@WebServlet("/AddProduct")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 5, maxFileSize = 1024 * 1024 * 20, maxRequestSize = 1024 * 1024 * 50)
public class AddProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDao;
    private ImageUtil imageUtil;
	private ValidationUtil validationUtil = new ValidationUtil();

    private static final String IMAGE_SUBFOLDER = "product";

    @Override
    public void init() throws ServletException {
        try {
            productDao = new ProductDAO(com.theYarnestShop.config.DatabaseConfig.getDatabaseConnection());
            imageUtil = new ImageUtil();
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            System.out.println("Received parameters:");
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                System.out.println(paramName + ": " + request.getParameter(paramName));
            }

            String productId = request.getParameter("productId");
            String productName = request.getParameter("productName");
            String category = request.getParameter("category");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String stockStr = request.getParameter("stock");

            // Validate form inputs
            if (productId == null || productId.trim().isEmpty()) {
                System.out.println("Product ID is empty or null when adding a new product.");
                request.setAttribute("errorMessage", "Product ID is required when adding a new product.");
                request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
            }else if (!validationUtil.isValidProductId(productId)) {
            	request.setAttribute("errorMessage", "Invalid product id, try again! ");
                request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
			}
            if (productName == null || productName.trim().isEmpty()) {
                System.out.println("Product Name is empty or null.");
                request.setAttribute("errorMessage", "Product Name is required.");
                request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
            }else if (!validationUtil.isValidProductName(productName)) {
            	request.setAttribute("errorMessage", "Invalid product name, try again! ");
                request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
			}
            if (category == null || category.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Category is required.");
                request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
            }else if (!validationUtil.isValidCategory(category)) {
            	request.setAttribute("errorMessage", "Category can only be Yarn, Bookmark, Clips, Keyring");
                request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
			}
            if (priceStr == null || priceStr.trim().isEmpty()) {
                System.out.println("Price is empty or null.");
                request.setAttribute("errorMessage", "Price is required and must be a number.");
                request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
            }
            if (stockStr == null || stockStr.trim().isEmpty()) {
                System.out.println("Stock is empty or null.");
                request.setAttribute("errorMessage", "Stock is required and must be an integer.");
                request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
            }

            float price = Float.parseFloat(priceStr);
            int stock = Integer.parseInt(stockStr);
            
            if (!validationUtil.isValidPrice(price)) {
            	request.setAttribute("errorMessage", "Invalid price, try again! ");
                request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
			}
            else if (!validationUtil.isValidStock(stock)) {
            	request.setAttribute("errorMessage", "Invalid stock, try again! ");
                request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
			}

            // Check if productId already exists
            ProductModel existingProduct = productDao.getProductById(productId);
            if (existingProduct != null) {
                System.out.println("Product ID " + productId + " already exists in the database.");
                request.setAttribute("errorMessage", "Product ID " + productId + " already exists. Please use a unique ID.");
                request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
            }
            
         // Check if productName already exists
            ProductModel existingProductName = productDao.getProductByName(productName);
            if (existingProductName != null) {
                System.out.println("Product Name " + productName + " already exists in the database.");
                request.setAttribute("errorMessage", "Product Name " + productName + " already exists. Please use a unique product name.");
                request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
            }

            // Verify database connection
            if (productDao.getConnection() == null || productDao.getConnection().isClosed()) {
                throw new SQLException("Database connection is not available.");
            }

            Part imagePart = request.getPart("imageFile");
            String image = null;

            if (imagePart != null && imagePart.getSize() > 0) {
                String deploymentPath = request.getServletContext().getRealPath("") + File.separator + "resources" + File.separator + "image" + File.separator + IMAGE_SUBFOLDER;
                String devPath = imageUtil.getSavePath(IMAGE_SUBFOLDER);

                // Save to deployment path (server runtime)
                String imageName = ImageUtil.saveImage(imagePart, deploymentPath);
                if (!imageName.equals("default.jpg")) {
                    // Save to development path (source folder)
                    ImageUtil.saveImage(imagePart, devPath);
                    image = "resources/image/" + IMAGE_SUBFOLDER + "/" + imageName;
                    System.out.println("Image path set: " + image);
                } else {
                    System.out.println("Failed to save image.");
                    image = null; // Allow NULL in database
                }
            }

            ProductModel newProduct = new ProductModel();
            newProduct.setProduct_id(productId);
            newProduct.setProduct_name(productName);
            newProduct.setCategory(category);
            newProduct.setDescription(description);
            newProduct.setPrice(price);
            newProduct.setImage(image);
            newProduct.setStock(stock);

            productDao.insertProduct(newProduct);
            System.out.println("New product added: " + productName);

            response.sendRedirect(request.getContextPath() + "/ProductList");

        } catch (SQLException | NumberFormatException | IllegalStateException e) {
            System.out.println("Error while adding product: " + e.getMessage());
            e.printStackTrace();
            String errorMessage = e instanceof IllegalStateException ?
                "File upload failed: File size exceeds limit (max 20MB) or request size exceeds limit (max 50MB)." :
                "Failed to add product: " + e.getMessage();
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/jspfiles/addproduct.jsp").forward(request, response);
        }
    }
}