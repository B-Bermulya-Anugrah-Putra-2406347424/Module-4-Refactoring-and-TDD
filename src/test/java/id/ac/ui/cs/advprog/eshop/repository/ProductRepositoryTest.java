package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepositoryImpl productRepository;

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testCreateProductWithNegativeQuantity() {
        Product product = new Product();
        product.setProductName("Mangga Hitam");

        assertThrows(IllegalArgumentException.class, () -> {
            product.setProductQuantity(-5);
        });
    }

    @Test
    void testUpdateProductWithNegativeQuantity() {
        Product product = new Product();
        product.setProductName("Mangga Hitam");
        product.setProductQuantity(10);
        productRepository.create(product);

        assertThrows(IllegalArgumentException.class, () -> {
            product.setProductQuantity(-1);
        });
    }

    @Test
    void testUpdateSuccess() {
        Product product = new Product();
        product.setProductName("Mangga Hitam");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId(product.getProductId());
        updatedProduct.setProductName("Mangga Putih");
        updatedProduct.setProductQuantity(50);

        productRepository.update(updatedProduct);

        Product result = productRepository.findById(product.getProductId());
        assertEquals("Mangga Putih", result.getProductName());
        assertEquals(50, result.getProductQuantity());
    }

    @Test
    void testUpdateNotFound() {
        Product updatedProduct = new Product();
        updatedProduct.setProductId("id salah ngawur super");
        updatedProduct.setProductName("none");

        Product result = productRepository.update(updatedProduct);
        assertNull(result);
    }

    @Test
    void testUpdateLoopBranch() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Mangga Hitam");
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductName("Mangga Putih");
        productRepository.create(product2);

        Product updatedProduct = new Product();
        updatedProduct.setProductName("Mangga Fake");

        Product result = productRepository.update(updatedProduct);

        assertNull(result);

        assertEquals("Mangga Hitam", productRepository.findById("eb558e9f-1c39-460e-8860-71af6af63bd6").getProductName());
    }

    @Test
    void testDeleteSuccess() {
        Product product = new Product();
        product.setProductName("Mangga Hitam");
        product.setProductQuantity(10);
        productRepository.create(product);

        productRepository.delete(product.getProductId());

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindByIdSuccess() {
        Product product = new Product();
        product.setProductName("Mangga Hitam");
        productRepository.create(product);

        Product found = productRepository.findById(product.getProductId());
        assertNotNull(found);
        assertEquals(product.getProductName(), found.getProductName());
    }

    @Test
    void testFindByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.findById("id ngasal");
        });
    }
}