package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

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
}