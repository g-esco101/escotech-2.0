package com.escotech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageProcessor {

    private final static Logger logger = LoggerFactory.getLogger(ImageProcessor.class);

    public static byte[] resizeImage(BufferedImage originalImage, String fileName, String fileExtension,
                                        int targetWidth, int targetHeight) throws IOException {
        Image scaledInstance = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(scaledInstance, 0, 0, null);
        graphics2D.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(resizedImage, fileExtension, baos);
            baos.close();
        } catch (IOException e) {
            String info = String.format("getScaledImage - IOException for file %s - message: %s.", fileName, e.getMessage());
            logger.error(info);
        }
        return baos.toByteArray();
    }

    public static byte[] getScaledImage(BufferedImage originalImage, String fileName, String fileExtension,
                                        int boundaryWidth, int boundaryHeight) throws IOException {

        int[] boundaries = getScaledDimension(originalImage.getWidth(), originalImage.getHeight(), boundaryWidth, boundaryHeight);
        Image scaledInstance = originalImage.getScaledInstance(boundaries[0], boundaries[1], Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(boundaries[0], boundaries[1], BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(scaledInstance, 0, 0, null);
        graphics2D.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(resizedImage, fileExtension, baos);
            baos.close();
        } catch (IOException e) {
            String info = String.format("getScaledImage - IOException for file %s - message: %s.", fileName, e.getMessage());
            logger.error(info);
        }
        return baos.toByteArray();
    }

    public static byte[] compressImage(MultipartFile mpFile, String fileName, String fileExtension, float qualityFactor) {
        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName(fileExtension).next();
        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(qualityFactor);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // MemoryCacheImageOutputStream: An implementation of ImageOutputStream that writes its output to a regular OutputStream.
        // A memory buffer is used to cache at least the data between the discard position and the current write position.
        ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(baos);
        imageWriter.setOutput(imageOutputStream);
        BufferedImage originalImage = null;
        try (InputStream inputStream = mpFile.getInputStream()) {
            originalImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            String info = String.format("compressImage - bufferedImage (file %s)- IOException - message: %s ", fileName, e.getMessage());
            logger.error(info);
            return baos.toByteArray();
        }
        IIOImage image = new IIOImage(originalImage, null, null);
        try {
            imageWriter.write(null, image, imageWriteParam);
        } catch (IOException e) {
            String info = String.format("compressImage - imageWriter (file %s)- IOException - message: %s ", fileName, e.getMessage());
            logger.error(info);
        } finally {
            imageWriter.dispose();
        }

        return baos.toByteArray();
    }


    /** Calculates the scaled dimensions of the image such that the aspect ratio is preserved.
     *
     * @param imageWidth the original width of the image.
     * @param imageHeight the original height of the image.
     * @param boundaryWidth the scaled width of the image.
     * @param boundaryHeight the scaled height of the image.
     * @return the boundary (i.e. the scaled width & the scaled height of the image.
     */
    private static int[] getScaledDimension(int imageWidth, int imageHeight, int boundaryWidth, int boundaryHeight) {
        int[] dimensions = new int[2];
        double widthRatio = (double) boundaryWidth / imageWidth;
        double heightRatio = (double) boundaryHeight / imageHeight;
        double ratio = Math.min(widthRatio, heightRatio);
        dimensions[0] = (int) (imageWidth  * ratio);
        dimensions[1] = (int) (imageHeight  * ratio);
        return dimensions;
    }
}
