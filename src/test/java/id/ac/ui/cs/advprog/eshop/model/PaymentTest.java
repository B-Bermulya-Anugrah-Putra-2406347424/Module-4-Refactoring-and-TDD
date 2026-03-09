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

    @Test
    void testVoucherValidationBranches() {
        Map<String, String> data = new HashMap<>();

        data.put("voucherCode", "ESHOP12345678901");
        assertEquals("REJECTED", new VoucherPayment("1", data).getStatus());

        data.clear();
        data.put("voucherCode", "ABCDE12345678901");
        assertEquals("REJECTED", new VoucherPayment("2", data).getStatus());

        data.clear();
        data.put("voucherCode", "ESHOP1234567890A");
        assertEquals("REJECTED", new VoucherPayment("3", data).getStatus());
    }

    @Test
    void testBankTransferBranches() {
        Map<String, String> data = new HashMap<>();

        data.put("bankName", "");
        data.put("referenceCode", "123");
        assertEquals("REJECTED", new BankTransferPayment("1", data).getStatus());

        data.clear();
        data.put("bankName", "BCA");
        data.put("referenceCode", "  ");
        assertEquals("REJECTED", new BankTransferPayment("2", data).getStatus());
    }

    @Test
    void testConstructorVoucherSuccess() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP12345678ABC");
        VoucherPayment payment = new VoucherPayment("v-1", data);
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("VOUCHER", payment.getMethod());
    }

    @Test
    void testValidateVoucherNull() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", null);
        VoucherPayment payment = new VoucherPayment("v-2", data);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testValidateVoucherInvalidLength() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP123");
        VoucherPayment payment = new VoucherPayment("v-3", data);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testValidateVoucherInvalidPrefix() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "NOTSHOP123456789");
        VoucherPayment payment = new VoucherPayment("v-4", data);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testValidateVoucherInvalidDigitCount() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOPABCDEFG1234");
        VoucherPayment payment = new VoucherPayment("v-5", data);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testBankTransferSuccess() {
        Map<String, String> data = new HashMap<>();
        data.put("bankName", "BCA");
        data.put("referenceCode", "REF123");
        BankTransferPayment payment = new BankTransferPayment("b-1", data);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testBankNameNull() {
        Map<String, String> data = new HashMap<>();
        data.put("bankName", null);
        data.put("referenceCode", "REF123");
        assertEquals("REJECTED", new BankTransferPayment("b-2", data).getStatus());
    }

    @Test
    void testBankNameEmpty() {
        Map<String, String> data = new HashMap<>();
        data.put("bankName", "  ");
        data.put("referenceCode", "REF123");
        assertEquals("REJECTED", new BankTransferPayment("b-3", data).getStatus());
    }

    @Test
    void testReferenceCodeNull() {
        Map<String, String> data = new HashMap<>();
        data.put("bankName", "BCA");
        data.put("referenceCode", null);
        assertEquals("REJECTED", new BankTransferPayment("b-4", data).getStatus());
    }

    @Test
    void testReferenceCodeEmpty() {
        Map<String, String> data = new HashMap<>();
        data.put("bankName", "BCA");
        data.put("referenceCode", " ");
        assertEquals("REJECTED", new BankTransferPayment("b-5", data).getStatus());
    }
}
