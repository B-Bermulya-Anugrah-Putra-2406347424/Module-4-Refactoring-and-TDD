package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.BankTransferPayment;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.VoucherPayment;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    private final Map<String, BiFunction<String, Map<String, String>, Payment>> paymentCreators = new HashMap<>();

    public PaymentServiceImpl() {
        paymentCreators.put("VOUCHER", VoucherPayment::new);
        paymentCreators.put("BANK_TRANSFER", BankTransferPayment::new);
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        BiFunction<String, Map<String, String>, Payment> creator = paymentCreators.get(method);

        if (creator == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + method);
        }

        Payment payment = creator.apply(order.getId(), paymentData);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        paymentRepository.save(payment);

        Order order = orderRepository.findById(payment.getId());
        if (order != null) {
            updateOrderStatusBasedOnPayment(order, status);
            orderRepository.save(order);
        }

        return payment;
    }

    private void updateOrderStatusBasedOnPayment(Order order, String paymentStatus) {
        if ("SUCCESS".equals(paymentStatus)) {
            order.setStatus(OrderStatus.SUCCESS.getValue());
        } else if ("REJECTED".equals(paymentStatus)) {
            order.setStatus(OrderStatus.FAILED.getValue());
        }
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.getAllPayments();
    }
}