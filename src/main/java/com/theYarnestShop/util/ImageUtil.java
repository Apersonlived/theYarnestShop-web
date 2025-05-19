package com.theYarnestShop.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.Part;

/**
 * Utility class for handling image uploads and file name generation.
 * Supports saving images to user or product directories with unique names.
 * 
 * author: Ekata Baral
 */
public class ImageUtil {

    /**
     * Extracts the original filename from the uploaded form part.
     *
     * @param part the uploaded file part
     * @return the extracted filename, or "default.jpg" if not found
     */
    public String getImageNameFromPart(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        String imageName = null;

        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                imageName = s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }

        return (imageName == null || imageName.isEmpty()) ? "default.jpg" : imageName;
    }

    /**
     * Uploads the given image part to the designated directory.
     *
     * @param part       the image file part
     * @param rootPath   (currently unused, kept for future extension)
     * @param saveFolder the folder name under the predefined path
     * @return true if the image is saved successfully, false otherwise
     */
    public boolean uploadImage(Part part, String rootPath, String saveFolder) {
        String savePath = getSavePath(saveFolder);
        File fileSaveDir = new File(savePath);

        if (!fileSaveDir.exists() && !fileSaveDir.mkdirs()) {
            System.out.println("Failed to create directory: " + savePath);
            return false;
        }

        try {
            String originalName = getImageNameFromPart(part);
            String extension = originalName.substring(originalName.lastIndexOf('.'));
            String uniqueFileName = "IMG_" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()) + extension;

            String filePath = savePath + File.separator + uniqueFileName;
            part.write(filePath);

            System.out.println("Image saved to: " + filePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the full absolute system path for user profile image storage.
     *
     * @param saveFolder optional subfolder to be added
     * @return complete system path
     */
    public String getSavePath(String saveFolder) {
        return "C:\\Users\\hp\\eclipse-workspace\\theYarnestShop\\src\\main\\webapp\\resources\\image" 
                + File.separator + saveFolder;
    }

    /**
     * Saves the uploaded image with a unique filename into the specified directory.
     *
     * @param part      the image file part
     * @param uploadDir the directory where image will be stored
     * @return the generated filename (or "default.jpg" on failure)
     */
    public static String saveImage(Part part, String uploadDir) {
        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String originalFileName = part.getSubmittedFileName();
            String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            String uniqueName = "IMG_" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()) + extension;

            part.write(uploadDir + File.separator + uniqueName);
            System.out.println("Image saved via static method: " + uniqueName);
            return uniqueName;
        } catch (Exception e) {
            e.printStackTrace();
            return "default.jpg";
        }
    }
}