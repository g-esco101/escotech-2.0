package com.escotech.security;

import com.github.database.rider.junit5.DBUnitExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

//@WebMvcTest(OrderController.class)
@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
public class OrderControllerSecurityTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private WebApplicationContext wac;
//
//    @MockBean
//    private OrderService service;
//
//    @MockBean
//    private MapStructMapper mapper;
//
//    @BeforeEach
//    void setup(WebApplicationContext wac) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
//                .apply(springSecurity())
//                .apply(sharedHttpSession())
//                .build();
//    }
//
//    @Test
//    @DisplayName("Anonymous user cannot get orders")
//    public void anonymousCannotGetOrders() throws Exception {
//        this.mockMvc
//                .perform(get("/admin/orders"))
//                .andExpect(status().isFound())
//                .andExpect(header().exists("Location"))
//                .andExpect(header().string("Location", Matchers.containsString("/oauth2/authorization/cognito")));
//    }
//
//    @Test
//    @DisplayName("Anonymous user cannot get order")
//    public void anonymousCannotGetOrder() throws Exception {
//        this.mockMvc
//                .perform(get("/admin/order/0"))
//                .andExpect(status().isFound())
//                .andExpect(header().exists("Location"))
//                .andExpect(header().string("Location", Matchers.containsString("/oauth2/authorization/cognito")));
//    }
//
//    @Test
//    @DisplayName("Anonymous user cannot delete order")
//    public void anonymousCannotDeleteOrder() throws Exception {
//        this.mockMvc
//                .perform(
//                        get("/admin/order/delete/0")
//                )
//                .andExpect(status().isFound())
//                .andExpect(header().exists("Location"))
//                .andExpect(header().string("Location", Matchers.containsString("/oauth2/authorization/cognito")));
//    }
//
//    @Test
//    @DisplayName("Anonymous user cannot create order")
//    public void anonymousCannotCreateOrder() throws Exception {
//        this.mockMvc
//                .perform(get("/admin/order/create"))
//                .andExpect(status().isFound())
//                .andExpect(header().exists("Location"))
//                .andExpect(header().string("Location", Matchers.containsString("/oauth2/authorization/cognito")));
//    }
//
//    @Test
//    @DataSet("products.yml")
//    public void adminOrdersWhenUserIsAuthenticated() throws Exception {
//        this.mockMvc
//                .perform(get("/admin/orders").with(SecurityMockMvcRequestPostProcessors.user("govi")))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(4))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].getId()").value("0"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[1].getId()").value("1"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[2].getId()").value("2"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[3].getId()").value("3"));
//    }
}
