package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public String createOrderPage() {
        return "CreateOrder";
    }

    @PostMapping("/create")
    public String createOrderPost(@RequestParam("author") String author) {
        // kudu create dummy
        List<Product> dummyProducts = new ArrayList<>();
        Product product = new Product();
        product.setProductId("dummy-1");
        product.setProductName("Dummy Product");
        product.setProductQuantity(1);
        dummyProducts.add(product);

        Order order = new Order(
                UUID.randomUUID().toString(),
                dummyProducts,
                System.currentTimeMillis(),
                author
        );
        orderService.createOrder(order);

        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String historyOrderPage() {
        return "OrderHistory";
    }

    @PostMapping("/history")
    public String historyOrderPost(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);

        model.addAttribute("orders", orders);
        model.addAttribute("author", author);

        return "OrderList";
    }
}