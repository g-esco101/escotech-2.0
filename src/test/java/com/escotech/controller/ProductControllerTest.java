package com.escotech.controller;

import com.escotech.TestUtils;
import com.escotech.dto.ProductView;
import com.escotech.entity.Product;
import com.escotech.service.ImageService;
import com.escotech.service.MapStructMapper;
import com.escotech.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @MockBean
    private ProductService service;
    @MockBean
    private MapStructMapper mapper;
    @MockBean
    private ImageService imageService;

    @BeforeEach
    void setUP() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
//                .apply(sharedHttpSession())
                .build();
    }

    @Test
    @DisplayName("/admin/allProducts - gets 3 products - success")
    public void getAllProduct() throws Exception {

        List<Product> products = new ArrayList<>();
        products.add(TestUtils.product1(0));
        products.add(TestUtils.product2(5));
        products.add(TestUtils.product3(10));

        ProductView productView1 = TestUtils.productView1(0);
        ProductView productView2 = TestUtils.productView2(5);
        ProductView productView3 = TestUtils.productView3(10);
        List<ProductView> productViews = new ArrayList<>();
        productViews.add(productView1);
        productViews.add(productView2);
        productViews.add(productView3);

        when(service.findAll()).thenReturn(products);
        when(mapper.productToProductView(any())).thenReturn(productView1, productView2, productView3);

        this.mockMvc
                .perform(
                        get("/admin/allProducts")
                                .with(csrf())
                                .with(SecurityMockMvcRequestPostProcessors.oauth2Client("1"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("product/allProducts"))
                .andExpect(model().attribute("products", productViews));
    }

//    @Test
//    @DisplayName("/admin/allProducts - anonymous - success")
//    public void getAllProductAnonymous() throws Exception {
//        this.mockMvc
//                .perform(
//                        get("/admin/allProducts")
//                )
//                .andExpect(status().isFound())
//                .andExpect(header().exists("Location"))
//                .andExpect(header().string("Location", Matchers.containsString("oauth2/authorization/ogniton")));
//    }

    @Test
    @DisplayName("/products - gets 2 products - success")
    public void getProducts() throws Exception {

        List<Product> products = new ArrayList<>();
        products.add(TestUtils.product2(5));
        products.add(TestUtils.product3(10));

        ProductView productView2 = TestUtils.productView2(5);
        ProductView productView3 = TestUtils.productView3(10);
        List<ProductView> productViews = new ArrayList<>();
        productViews.add(productView2);
        productViews.add(productView3);

        when(service.findAllByInventoryCountGreaterThan(0)).thenReturn(products);
        when(mapper.productToProductView(any())).thenReturn(productView2, productView3);

        this.mockMvc
                .perform(
                        get("/products")
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("product/products"))
                .andExpect(model().attribute("products", productViews));
    }

    @Test
    @DisplayName("/products/{id} - get product 2 - success")
    public void getProductById() throws Exception {
        Product product2 = TestUtils.product2(5);
        Optional<Product> productOpt = Optional.of(product2);
        ProductView productView2 = TestUtils.productView2(5);

        when(service.findById(2L)).thenReturn(productOpt);
        when(mapper.productToProductView(any())).thenReturn(productView2);

        this.mockMvc
                .perform(
                        get("/products/2")
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("product/product"))
                .andExpect(model().attribute("product", productView2));
    }

    @Test
    @DisplayName("/products/{id} - get product - failure")
    public void getProductByIdFail() throws Exception {
        ProductView productView2 = TestUtils.productView2(5);
        ProductView productView3 = TestUtils.productView3(10);
        List<ProductView> productViews = new ArrayList<>();
        productViews.add(productView2);
        productViews.add(productView3);

        List<Product> products = new ArrayList<>();
        products.add(TestUtils.product2(5));
        products.add(TestUtils.product3(10));

        when(service.findById(100L)).thenReturn(Optional.empty());
        when(service.findAllByInventoryCountGreaterThan(0)).thenReturn(products);
        when(mapper.productToProductView(any())).thenReturn(productView2, productView3);

        this.mockMvc
                .perform(
                        get("/products/100")
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(view().name("product/products"))
                .andExpect(model().attribute("products", productViews));
    }
}
