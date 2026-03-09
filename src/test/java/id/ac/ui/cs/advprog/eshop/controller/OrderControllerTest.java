package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.model.VoucherPayment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void testCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateOrder"));
    }

    @Test
    void testCreateOrderPost() throws Exception {
        mockMvc.perform(post("/order/create")
                        .param("author", "Bambang"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testHistoryOrderPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderHistory"));
    }

    @Test
    void testHistoryOrderPost() throws Exception {
        when(orderService.findAllByAuthor("Bambang")).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/order/history")
                        .param("author", "Bambang"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderList"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void testPayOrderPage() throws Exception {
        mockMvc.perform(get("/order/pay/123"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderPayment"))
                .andExpect(model().attributeExists("orderId"));
    }

    @Test
    void testPayOrderPost() throws Exception {
        List<Product> dummyProducts = new ArrayList<>();
        Product product = new Product();
        product.setProductId("dummy-1");
        product.setProductName("Kecap");
        product.setProductQuantity(1);
        dummyProducts.add(product);

        Order order = new Order("123", dummyProducts, 123L, "Bambang");
        when(orderService.findById("123")).thenReturn(order);

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("method", "VOUCHER");
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new VoucherPayment("pay-123", paymentData);

        when(paymentService.addPayment(any(Order.class), anyString(), any(Map.class))).thenReturn(payment);

        mockMvc.perform(post("/order/pay/123")
                        .param("method", "VOUCHER")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderPaymentSuccess"))
                .andExpect(model().attributeExists("paymentId"));
    }
}