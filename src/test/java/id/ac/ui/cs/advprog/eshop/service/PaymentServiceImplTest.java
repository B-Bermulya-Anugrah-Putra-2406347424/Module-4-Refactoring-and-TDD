package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.BankTransferPayment;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.model.VoucherPayment;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderRepository orderRepository;

    private Order order;
    private Map<String, String> voucherData;
    private Map<String, String> bankData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("prod-1");
        product.setProductName("Kecap");
        product.setProductQuantity(1);
        products.add(product);

        order = new Order("order-100", products, 1708560000L, "Safira");

        voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP1234ABC5678");

        bankData = new HashMap<>();
        bankData.put("bankName", "BCA");
        bankData.put("referenceCode", "REF12345");
    }

    @Test
    void testAddPaymentVoucher() {
        Payment payment = new VoucherPayment(order.getId(), voucherData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "VOUCHER", voucherData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertInstanceOf(VoucherPayment.class, result);
        assertEquals(order.getId(), result.getId());
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void testAddPaymentBankTransfer() {
        Payment payment = new BankTransferPayment(order.getId(), bankData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", bankData);

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertInstanceOf(BankTransferPayment.class, result);
        assertEquals(order.getId(), result.getId());
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void testAddPaymentUnsupported() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.addPayment(order, "PAYLATER", new HashMap<>());
        });
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = new VoucherPayment(order.getId(), voucherData);
        doReturn(order).when(orderRepository).findById(payment.getId());
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(payment);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = new BankTransferPayment(order.getId(), bankData);
        doReturn(order).when(orderRepository).findById(payment.getId());
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(payment);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testGetPayment() {
        Payment payment = new VoucherPayment(order.getId(), voucherData);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());
        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetAllPayments() {
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(new VoucherPayment(order.getId(), voucherData));
        doReturn(paymentList).when(paymentRepository).getAllPayments();

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(1, result.size());
    }
}