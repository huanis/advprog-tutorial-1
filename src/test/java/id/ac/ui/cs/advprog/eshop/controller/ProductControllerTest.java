package id.ac.ui.cs.advprog.eshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    private Product product;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp(){
        this.product = new Product();
        this.product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        this.product.setProductName("Sampo Cap Bambang");
        this.product.setProductQuantity(100);

        this.mapper = new ObjectMapper();
    }

    @Test
    void testProductList() throws Exception {
        lenient().when(service.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("productListPage"))
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("/productList"));
    }

    @Test
    void testCreateProductPage() throws Exception {

        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createProductPage"))
                .andExpect(model().attributeExists("product"))
                .andExpect(view().name("/createProduct"));

    }

    @Test
    void testCreateProductPost() throws Exception {
        lenient().when(service.create(any())).thenReturn(this.product);

        mockMvc.perform(post("/product/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(this.product)))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("createProductPost"))
                .andExpect(view().name("redirect:list"));
    }

    @Test
    void testUpdateProductPage() throws Exception {
        lenient().when(service.findByProductId(any())).thenReturn(this.product);

        mockMvc.perform(get("/product/update/dummy"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updateProductPage"))
                .andExpect(model().attributeExists("product"))
                .andExpect(view().name("/updateProduct"));
    }

    @Test
    void testUpdateProductPageNotExist() throws Exception {
        lenient().when(service.findByProductId(any())).thenReturn(null);

        mockMvc.perform(get("/product/update/dummy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("updateProductPage"))
                .andExpect(view().name("redirect:list"));
    }

    @Test
    void testUpdateProductPost() throws Exception {
        lenient().doNothing().when(service).update(any());

        mockMvc.perform(post("/product/update")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.mapper.writeValueAsString(this.product)))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("updateProductPost"))
                .andExpect(view().name("redirect:list"));
    }

    @Test
    void testDeleteProductPost() throws Exception {
        lenient().doNothing().when(service).update(any());

        mockMvc.perform(post("/product/delete")
                        .param("productId", "dummy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("deleteProductPost"))
                .andExpect(view().name("redirect:list"));
    }

}
