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

    private static final String PRODUCT_MODEL_NAME = "product";
    private static final String REDIRECT_LIST = "redirect:list";

    @Autowired
    private ProductService service;

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute(PRODUCT_MODEL_NAME, product);
        return "CreateProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model) {
        service.create(product);
        return REDIRECT_LIST;
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "ProductList";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable String id, Model model) {
        Product product = service.findById(id);
        model.addAttribute(PRODUCT_MODEL_NAME, product);
        return "EditProduct";
    }

    @PostMapping("/edit")
    public String editProductPost(@ModelAttribute Product product) {
        service.update(product);
        return REDIRECT_LIST;
    }

    @GetMapping("/delete/{id}")
    public String deleteProductPage(@PathVariable String id, Model model) {
        Product product = service.findById(id);
        model.addAttribute(PRODUCT_MODEL_NAME, product);
        return "DeleteProduct";
    }

    @PostMapping("/delete")
    public String deleteProductPost(@RequestParam("productId") String id) {
        service.delete(id);
        return REDIRECT_LIST;
    }
}