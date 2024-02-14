package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService service;

    private static final String REDIRECT_TO_LIST = "redirect:list";

    @Autowired
    public ProductController(ProductService service){
        this.service = service;
    }

    @GetMapping("/create")
    public String createProductPage(Model model){
        Product product = new Product();
        model.addAttribute("product", product);
        return "createProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model){
        service.create(product);
        return REDIRECT_TO_LIST;
    }

    @GetMapping("/list")
    public String productListPage(Model model){
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "productList";
    }

    @GetMapping("/update/{productId}")
    public String updateProductPage(Model model, @PathVariable String productId){
        Product product = service.findByProductId(productId);
        if (product == null) return "redirect:list";
        model.addAttribute("product", product);
        return "updateProduct";
    }

    @PostMapping("/update")
    public String updateProductPost(@ModelAttribute Product product, Model model){
        service.update(product);
        return REDIRECT_TO_LIST;
    }

    @PostMapping("/delete")
    public String deleteProductPost(@RequestParam String productId, Model model){
        service.delete(productId);
        return REDIRECT_TO_LIST;
    }
}
