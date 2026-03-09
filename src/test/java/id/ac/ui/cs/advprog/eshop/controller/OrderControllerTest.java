package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

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
        id.ac.ui.cs.advprog.eshop.model.Order order = new id.ac.ui.cs.advprog.eshop.model.Order("123", new ArrayList<>(), 123L, "Bambang");
        when(orderService.findById("123")).thenReturn(order);

        java.util.Map<String, String> paymentData = new java.util.HashMap<>();
        paymentData.put("method", "VOUCHER");
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        id.ac.ui.cs.advprog.eshop.model.Payment payment = new id.ac.ui.cs.advprog.eshop.model.VoucherPayment("pay-123", paymentData);

        when(paymentService.addPayment(any(), anyString(), any(java.util.Map.class))).thenReturn(payment);

        mockMvc.perform(post("/order/pay/123")
                        .param("method", "VOUCHER")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("OrderPaymentSuccess"))
                .andExpect(model().attributeExists("paymentId"));
    }
}