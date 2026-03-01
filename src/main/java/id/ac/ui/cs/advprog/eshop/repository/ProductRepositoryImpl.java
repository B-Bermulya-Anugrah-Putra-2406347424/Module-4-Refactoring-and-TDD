package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(String id) {
        return productData.stream()
                .filter(p -> p.getProductId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
    }

    public Product update(Product updatedProduct) {
        for (int index = 0; index < productData.size(); index++) {
            Product product = productData.get(index);
            if (product.getProductId().equals(updatedProduct.getProductId())) {
                productData.set(index, updatedProduct);
                return updatedProduct;
            }
        }
        return null;
    }

    public void delete(String id) {
        productData.removeIf(product -> product.getProductId().equals(id));
    }
}
