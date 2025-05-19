package com.theYarnestShop.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

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
 * Controller for editing existing products.
 */
/**
 * Servlet responsible for editing existing products in the catalog.
 * Allows administrators to view and update product details such as name,
 * category, description, price, stock, and image.
 */
@WebServlet("/EditProduct")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 5,   // 5MB
    maxFileSize = 1024 * 1024 * 20,        // 20MB
    maxRequestSize = 1024 * 1024 * 50      // 50MB
)
public class EditProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProductDAO productDao;
    private ImageUtil imageUtil;
    private ValidationUtil validationUtil = new ValidationUtil();

    private static final String IMAGE_SUBFOLDER = "product";

    /**
     * Initializes the servlet by setting up the DAO and utility classes.
     *
     * @throws ServletException if initialization fails due to DB connection or class loading issues
     */
    @Override
    public void init() throws ServletException {
        try {
            productDao = new ProductDAO(com.theYarnestShop.config.DatabaseConfig.getDatabaseConnection());
            imageUtil = new ImageUtil();
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException(e);
        }
    }

    /**
     * Handles GET requests to the EditProduct servlet.
     * Retrieves the existing product data and displays it on the edit form.
     *
     * @param request  HttpServletRequest containing the product ID parameter
     * @param response HttpServletResponse used to forward to the JSP
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during forwarding
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productId = request.getParameter("productId");

        if (productId != null && !productId.isEmpty()) {
            try {
                ProductModel product = productDao.getProductById(productId);
                if (product != null) {
                    System.out.println("Fetched product for editing: " + product.getProduct_name());
                    request.setAttribute("product", product);
                } else {
                    request.setAttribute("errorMessage", "Product with ID " + productId + " not found.");
                }
            } catch (SQLException e) {
                request.setAttribute("errorMessage", "Failed to fetch product: " + e.getMessage());
            }
        } else {
            request.setAttribute("errorMessage", "Product ID is required for editing.");
        }

        request.getRequestDispatcher("/WEB-INF/jspfiles/editproduct.jsp").forward(request, response);
    }

    /**
     * Handles POST requests to update an existing product.
     * Performs field validation, handles image upload, and persists updates.
     *
     * @param request  HttpServletRequest with form input and image file
     * @param response HttpServletResponse used for redirection or forwarding
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during file handling or forwarding
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Extract form parameters
            String productId = request.getParameter("productId");
            String productName = request.getParameter("productName");
            String category = request.getParameter("category");
            String description = request.getParameter("description");
            float price = Float.parseFloat(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            String existingImage = request.getParameter("existingImage");

            // Retrieve the existing product from the database
            ProductModel existingProduct = productDao.getProductById(productId);
            if (existingProduct == null) {
                request.setAttribute("errorMessage", "Product with ID " + productId + " not found.");
                request.getRequestDispatcher("/WEB-INF/jspfiles/editproduct.jsp").forward(request, response);
                return;
            }

            // Validate product fields
            if (productName == null || productName.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Product Name is required.");
                request.getRequestDispatcher("/WEB-INF/jspfiles/editproduct.jsp").forward(request, response);
                return;
            } else if (!validationUtil.isValidProductName(productName)) {
                request.setAttribute("errorMessage", "Invalid product name, try again!");
                request.getRequestDispatcher("/WEB-INF/jspfiles/editproduct.jsp").forward(request, response);
                return;
            }

            if (!validationUtil.isValidPrice(price)) {
                request.setAttribute("errorMessage", "Invalid price, try again!");
                request.getRequestDispatcher("/WEB-INF/jspfiles/editproduct.jsp").forward(request, response);
                return;
            }

            if (!validationUtil.isValidStock(stock)) {
                request.setAttribute("errorMessage", "Invalid stock, try again!");
                request.getRequestDispatcher("/WEB-INF/jspfiles/editproduct.jsp").forward(request, response);
                return;
            }

            if (category == null || category.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Category is required.");
                request.getRequestDispatcher("/WEB-INF/jspfiles/editproduct.jsp").forward(request, response);
                return;
            } else if (!validationUtil.isValidCategory(category)) {
                request.setAttribute("errorMessage", "Category can only be Yarn, Bookmark, Clips, Keyring.");
                request.getRequestDispatcher("/WEB-INF/jspfiles/editproduct.jsp").forward(request, response);
                return;
            }

            // Handle optional image upload
            Part imagePart = request.getPart("imageFile");
            String image = existingImage;

            if (imagePart != null && imagePart.getSize() > 0) {
                String deploymentPath = request.getServletContext().getRealPath("") + File.separator +
                        "resources" + File.separator + "image" + File.separator + IMAGE_SUBFOLDER;
                String devPath = imageUtil.getSavePath(IMAGE_SUBFOLDER);

                String imageName = ImageUtil.saveImage(imagePart, deploymentPath);
                if (!imageName.equals("default.jpg")) {
                    ImageUtil.saveImage(imagePart, devPath);
                    image = "resources/image/" + IMAGE_SUBFOLDER + "/" + imageName;
                    System.out.println("Image path updated: " + image);
                } else {
                    System.out.println("Failed to update image.");
                }
            }

            // Construct updated product model
            ProductModel updatedProduct = new ProductModel();
            updatedProduct.setProduct_id(productId);
            updatedProduct.setProduct_name(productName);
            updatedProduct.setCategory(category);
            updatedProduct.setDescription(description);
            updatedProduct.setPrice(price);
            updatedProduct.setImage(image);
            updatedProduct.setStock(stock);

            // Update product in database
            productDao.updateProduct(updatedProduct);
            System.out.println("Product updated: " + productName);

            // Redirect to product list
            response.sendRedirect(request.getContextPath() + "/ProductList");

        } catch (SQLException | NumberFormatException | IllegalStateException e) {
            System.out.println("Error while editing product: " + e.getMessage());
            e.printStackTrace();

            String errorMessage = (e instanceof IllegalStateException)
                    ? "File upload failed: File size exceeds limit (max 20MB) or request size exceeds limit (max 50MB)."
                    : "Failed to update product: " + e.getMessage();

            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/jspfiles/editproduct.jsp").forward(request, response);
        }
    }
}
