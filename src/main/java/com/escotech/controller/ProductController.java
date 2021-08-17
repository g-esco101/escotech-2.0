package com.escotech.controller;

import com.escotech.entity.Image;
import com.escotech.entity.Product;
import com.escotech.form.ProductForm;
import com.escotech.dto.ProductView;
import com.escotech.service.ImageService;
import com.escotech.service.MapStructMapper;
import com.escotech.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class ProductController {

    private final static Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService service;

    private final MapStructMapper mapper;

    private final ImageService imageService;

    @Autowired
    public ProductController(ProductService service, MapStructMapper mapper, ImageService imageService) {
        this.service = service;
        this.mapper = mapper;
        this.imageService = imageService;
    }

    @GetMapping("/admin/allProducts")
    public String getAllProducts(final Model model) {
        logger.info("/admin/allProducts");
        List<ProductView> products = service.findAll().stream()
                                            .map(mapper::productToProductView)
                                            .collect(Collectors.toList());
        model.addAttribute("products", products);
        return "product/allProducts";
    }

    @GetMapping("/products")
    public String getProducts(final Model model) {
        logger.info("/products");
        List<ProductView> products = service.findAllByInventoryCountGreaterThan(0).stream()
                .map(mapper::productToProductView)
                .collect(Collectors.toList());
        model.addAttribute("products", products);
        return "product/products";
    }

    @GetMapping("/products/{id}")
    public String getProductById(final Model modelMap, @PathVariable("id") Long id) {
        logger.info("/products/{}", id);
        Optional<Product> productOpt = service.findById(id);
        if (productOpt.isEmpty()) {
            logger.error("getProductById - product {} is null", id);
            List<ProductView> products = service.findAllByInventoryCountGreaterThan(0).stream()
                    .map(mapper::productToProductView)
                    .collect(Collectors.toList());
            modelMap.addAttribute("products", products);
            return "product/products";
        }
        ProductView productView = mapper.productToProductView(productOpt.get());
        modelMap.addAttribute("product", productView);
        return "product/product";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateForm(final Model model, @PathVariable("id") Long id) {
        logger.info("/admin/product/update/{} - get", id);
        Optional<Product> prodOpt = service.findById(id);
        if (prodOpt.isEmpty()) {
            logger.error("updateForm - product not present - id: {}", id);
            model.addAttribute("products", service.findAll());
            return "product/allProducts";
        }
        Product product = prodOpt.get();
        ProductForm productForm = mapper.productToProductForm(product);
        model.addAttribute("productForm", productForm);
        return "product/productUpdate";
    }

    @PostMapping("/admin/product/update")
    public String update(final Model model, @Valid @ModelAttribute("productForm") ProductForm productForm,
                         BindingResult result, @RequestParam(value = "imageIds" , required = false) Long[] imageIds) {

        logger.info("/admin/product/update - post");
        if (result.hasErrors()) {
            return "product/productUpdate";
        }

        Optional<Product> prodOpt = service.findById(productForm.getId());
        if (prodOpt.isEmpty()) {
            logger.error("update - product not present - id: {}", productForm.getId());
            return "product/productUpdate";
        }
        Product product = prodOpt.get();
        mapper.productFormToProduct(productForm, product);
        String imgsNotAdded = "";
        try {
            imgsNotAdded = imageService.addImageToProduct(product, productForm);
        } catch (IOException e) {
            model.addAttribute("IOException", "Unable to read one or more images.");
            return "product/productUpdate";
        }
        if (!imgsNotAdded.isEmpty()) {
            model.addAttribute("imgsNotAdded", imgsNotAdded);
            return "product/productUpdate";
        }
        if (imageIds != null) {
            imageService.removeImages(product, imageIds);
        }
        try {
            service.update(product);
        } catch (Exception e) {
            logger.error("update -  service.update(product) - msg: {}", e.getMessage());
        }
        return String.format("redirect:/products/%s", product.getId());
    }

    // GET: Show product.
    @GetMapping("/admin/product/create")
    public String createForm(final Model model) {
        logger.info("/admin/product/create - get");
        model.addAttribute("productForm", new ProductForm());
        return "product/productCreate";
    }

    // POST: Save product
    @PostMapping("/admin/product/create")
    public String create(final Model model, @Valid @ModelAttribute("productForm") ProductForm productForm,
                         BindingResult result) {

        logger.info("/admin/product/create - post");
        if (result.hasErrors()) {
            return "product/productCreate";
        }
        Product product = mapper.productFormToPoduct(productForm);
        String imgsNotAdded = "";
        try {
            imgsNotAdded = imageService.addImageToProduct(product, productForm);
        } catch (IOException e) {
            model.addAttribute("IOException", "Unable to read one or more images.");
            return "product/productCreate";
        }
        if (!imgsNotAdded.isEmpty()) {
            model.addAttribute("imgsNotAdded", imgsNotAdded);
            return "product/productCreate";
        }
        service.create(product);
        return String.format("redirect:/products/%s", product.getId());
    }

    @RequestMapping({ "/admin/product/delete/{id}" })
    public String delete(final Model model, @PathVariable("id") Long id) {
        logger.info("/admin/product/delete/{}", id);
        service.deleteById(id);
        List<ProductView> products = service.findAll().stream()
                .map(mapper::productToProductView)
                .collect(Collectors.toList());
        model.addAttribute("products", products);
        return "product/allProducts";
    }

    @GetMapping("/product/image")
    public void productImage(HttpServletResponse response, @RequestParam("id") Long id,
                             @RequestParam("index") Integer index,
                             @RequestParam("name") String name) throws IOException {

        Optional<Product> productOpt = service.findById(id);
        if (productOpt.isEmpty()) {
            logger.error("productImage - product id - {} is null", id);
            return;
        }
        Product product = productOpt.get();
        byte [] imgArray = null;
        if (product.getImages().size() > 0) {
            Image pic = product.getImages().get(index);
            switch (name) {
                case "img300":
                    imgArray = pic.getImg300();
                    break;
                case "img600":
                    imgArray = pic.getImg600();
                    break;
                case "img1600":
                    imgArray = pic.getImg1600();
                    break;
                case "scaled100":
                    imgArray = pic.getScaled100();
                    break;
                case "scaled600":
                    imgArray = pic.getScaled600();
                    break;
                case "scaled1600":
                    imgArray = pic.getScaled1600();
                    break;
            }
        }

        response.setContentType("image/jpeg, image/jpg, image/png, image/gif, image/bmp, image/wbmp");
        response.getOutputStream().write(imgArray);
        response.getOutputStream().close();
    }
}