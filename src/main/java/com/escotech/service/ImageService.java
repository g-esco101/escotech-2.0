package com.escotech.service;

import com.escotech.entity.Image;
import com.escotech.entity.Product;
import com.escotech.form.ProductForm;
import com.escotech.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

//@CacheConfig(cacheNames = "imageCache")
@Service
public class ImageService {

    private final static Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository repository;

    @Autowired
    public ImageService(ImageRepository repository) {
            this.repository = repository;
    }

//    //    @Cacheable
//    @Transactional(readOnly = true)
//    public Optional<Image> findById(Long id) {
//        return repository.findById(id);
//    }
//    @Cacheable
//    @Transactional
//    public Optional<Image> findById(Long id) {
//        logger.info("find - id: {}", id);
//        return repository.findById(id);
//    }

//    @CacheEvict(key = "#id")
//    @Transactional
//    public void deleteById(Long id) {
//        logger.info("deleting - id: {}.", id);
//        repository.deleteById(id);
//    }


    /** Adds an images to a product.
     *
     * @param product - images are added to it.
     * @param productForm - has the images to add to product.
     * @return - image names that were not added.
     * @throws IOException
     */
    public String addImageToProduct(Product product, ProductForm productForm) throws IOException {
        StringJoiner imgsNotAdded = new StringJoiner(", ", "Unable to add: ", ".");
        imgsNotAdded.setEmptyValue("");
        for (MultipartFile data : productForm.getFileData()) {
            if (!data.isEmpty()) {
                String imageName = data.getOriginalFilename();
                String imageExtension = imageName.substring(imageName.lastIndexOf(".") + 1);
                BufferedImage originalImage = getOriginalImage(data, imageName);
                if (originalImage == null) {
                    imgsNotAdded.add(imageName);
                    continue;
                }
                byte[] img300, img600, img1600, imgScaled100, imgScaled600, imgScaled1600;
                img300 = ImageProcessor.resizeImage(originalImage, imageName, imageExtension, 300, 300);
                img600 = ImageProcessor.resizeImage(originalImage, imageName, imageExtension, 600, 600);
                img1600 = ImageProcessor.resizeImage(originalImage, imageName, imageExtension, 1600, 1600);

                imgScaled100 = ImageProcessor.getScaledImage(originalImage, imageName, imageExtension, 100, 100);
                imgScaled600 = ImageProcessor.getScaledImage(originalImage, imageName, imageExtension, 600, 600);
                imgScaled1600 = ImageProcessor.getScaledImage(originalImage, imageName, imageExtension,1600, 1600);

                Image image = new Image(imageName, img300, img600, img1600, imgScaled100, imgScaled600, imgScaled1600);
                product.getImages().add(image);
            }
        }
        return imgsNotAdded.toString();
    }

    private BufferedImage getOriginalImage(MultipartFile mpFile, String fileName) {
        BufferedImage originalImage = null;
        try (InputStream inputStream = mpFile.getInputStream()) {
            originalImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            String info = String.format("getOriginalImage - IOException for file %s - message: %s.", fileName, e.getMessage());
            logger.error(info);
        }
        return originalImage;
    }

    /** Removes an image from a product. When this product is saved to the db, the image will be removed from the db.
     *
     * @param product which will have the images removed.
     * @param imageIds the id of the images to removed.
     */
    public void removeImages(Product product, Long[] imageIds) {
        Set<Long> ids = new HashSet<>(Arrays.asList(imageIds));
        product.getImages().removeIf(img -> ids.contains(img.getId()));
    }
}
