package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentTest {

    @Test
    void testCreateVoucherPaymentSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new VoucherPayment("1", paymentData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreateVoucherPaymentRejected() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP123");

        Payment payment = new VoucherPayment("2", paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreateBankTransferPaymentSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF12345");

        Payment payment = new BankTransferPayment("3", paymentData);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreateBankTransferPaymentRejectedEmptyBank() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", "REF12345");

        Payment payment = new BankTransferPayment("4", paymentData);
        assertEquals("REJECTED", payment.getStatus());
    }
}
