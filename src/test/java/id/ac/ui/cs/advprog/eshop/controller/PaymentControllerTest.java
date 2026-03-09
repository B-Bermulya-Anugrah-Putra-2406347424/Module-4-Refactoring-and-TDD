package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void testPaymentDetailFormPage() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetailForm"));
    }

    @Test
    void testPaymentDetailPage() throws Exception {
        when(paymentService.getPayment("123")).thenReturn(null);

        mockMvc.perform(get("/payment/detail/123"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentDetail"))
                .andExpect(model().attributeExists("paymentId"));
    }

    @Test
    void testPaymentAdminListPage() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void testPaymentAdminDetailPage() throws Exception {
        when(paymentService.getPayment("123")).thenReturn(null);

        mockMvc.perform(get("/payment/admin/detail/123"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentAdminDetail"))
                .andExpect(model().attributeExists("paymentId"));
    }

    @Test
    void testPaymentAdminSetStatusPost() throws Exception {
        mockMvc.perform(post("/payment/admin/set-status/123")
                        .param("status", "SUCCESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/admin/list"));
    }
}