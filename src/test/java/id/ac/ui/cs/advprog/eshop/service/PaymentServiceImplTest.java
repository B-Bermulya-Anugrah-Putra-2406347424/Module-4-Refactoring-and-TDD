package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.*;
import id.ac.ui.cs.advprog.eshop.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PaymentServiceImpl service;

    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        products.add(product);
        sampleOrder = new Order("ORDER-101", products, System.currentTimeMillis(), "User A");
    }

    @Test
    void testAddPaymentVoucherSuccess() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOPABC12345678");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = service.addPayment(sampleOrder, "VOUCHER", data);

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        verify(paymentRepository, times(1)).save(any());
    }

    @Test
    void testAddPaymentUnsupportedMethod() {
        Map<String, String> data = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> {
            service.addPayment(sampleOrder, "GOPAY", data);
        });
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = new Payment("ORDER-101", "VOUCHER", new HashMap<>());
        when(orderRepository.findById("ORDER-101")).thenReturn(sampleOrder);

        service.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", sampleOrder.getStatus());
        verify(orderRepository, times(1)).save(sampleOrder);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = new Payment("ORDER-101", "VOUCHER", new HashMap<>());
        when(orderRepository.findById("ORDER-101")).thenReturn(sampleOrder);

        service.setStatus(payment, "REJECTED");

        assertEquals("FAILED", sampleOrder.getStatus());
        verify(orderRepository, times(1)).save(sampleOrder);
    }

    @Test
    void testSetStatusOrderNotFound() {
        Payment payment = new Payment("NON-EXISTENT", "VOUCHER", new HashMap<>());
        when(orderRepository.findById("NON-EXISTENT")).thenReturn(null);

        service.setStatus(payment, "SUCCESS");

        verify(orderRepository, times(0)).save(any());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testGetPayment() {
        Payment payment = new Payment("1", "VOUCHER", new HashMap<>());
        when(paymentRepository.findById("1")).thenReturn(payment);

        Payment result = service.getPayment("1");
        assertEquals(payment, result);
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment("1", "VOUCHER", new HashMap<>()));
        when(paymentRepository.getAllPayments()).thenReturn(payments);

        List<Payment> result = service.getAllPayments();
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateOrderStatusRejectedBranch() {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        Order order = new Order("1", products, 123L, "A");

        Payment payment = new Payment("1", "VOUCHER", new HashMap<>());
        when(orderRepository.findById("1")).thenReturn(order);

        service.setStatus(payment, "REJECTED");
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testUpdateOrderStatusOtherStatus() {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        Order order = new Order("1", products, 123L, "A");

        Payment payment = new Payment("1", "VOUCHER", new HashMap<>());
        when(orderRepository.findById("1")).thenReturn(order);

        service.setStatus(payment, "PENDING");
        assertEquals("WAITING_PAYMENT", order.getStatus());
    }
}