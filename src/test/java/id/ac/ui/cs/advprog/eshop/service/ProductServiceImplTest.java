package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl service;

    Product product;

    @BeforeEach
    void setUp(){
        this.product = new Product();
        this.product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        this.product.setProductName("Sampo Cap Bambang");
        this.product.setProductQuantity(100);
    }

    @Test
    void testCreateProduct(){
        Mockito.when(productRepository.create(this.product)).thenReturn(this.product);

        Product createdProduct = service.create(this.product);
        assertEquals(this.product, createdProduct);
    }

    @Test
    void testFindAllProduct(){
        List<Product> expectedProducts = new ArrayList<>(List.of(this.product));

        Mockito.when(productRepository.findAll()).thenReturn(expectedProducts.iterator());

        List<Product> products = service.findAll();
        assertEquals(expectedProducts.getFirst(), products.getFirst());
    }

    @Test
    void testFindByProductId(){
        Mockito.when(productRepository.findByProductId(this.product.getProductId())).thenReturn(this.product);

        Product returnedProduct = service.findByProductId(this.product.getProductId());
        assertEquals(this.product, returnedProduct);
    }

    @Test
    void testUpdateProduct(){
        service.update(this.product);

        Mockito.verify(productRepository, Mockito.times(1)).update(this.product);
    }

    @Test
    void testDeleteProduct(){
        service.delete(this.product.getProductId());

        Mockito.verify(productRepository, Mockito.times(1)).delete(this.product.getProductId());
    }
}
