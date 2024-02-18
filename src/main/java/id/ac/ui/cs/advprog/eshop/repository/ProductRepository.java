package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        if (product != null) {
            if (product.getProductId() == null){
                product.setProductId(UUID.randomUUID().toString());
            }
            if (!checkExist(product.getProductId())) {
                productData.add(product);
            }
        }
        return product;
    }

    private boolean checkExist(String productId){
        for (Product product : productData){
            if (product.getProductId().equals(productId)) return true;
        }
        return false;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findByProductId(String productId){
        for (Product product : productData){
            if (product.getProductId().equals(productId)){
                return product;
            }
        }
        return null;
    }

    public Product update(Product product) {
        for (Product oldProduct : productData){
            if (oldProduct.getProductId().equals(product.getProductId())){
                BeanUtils.copyProperties(product, oldProduct);
                return oldProduct;
            }
        }
        return null;
    }

    public void delete(String productId){
        productData.removeIf(product -> product.getProductId().equals(productId));
    }
}
