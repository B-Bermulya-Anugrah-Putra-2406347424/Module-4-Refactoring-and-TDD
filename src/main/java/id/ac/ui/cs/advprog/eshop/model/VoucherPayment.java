package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

public class VoucherPayment extends Payment {

    public VoucherPayment(String id, Map<String, String> paymentData) {
        super(id, "VOUCHER", paymentData);
        this.status = validateVoucher(paymentData.get("voucherCode")) ? "SUCCESS" : "REJECTED";
    }

    private boolean validateVoucher(String voucher) {
        if (voucher == null || voucher.length() != 16 || !voucher.startsWith("ESHOP")) {
            return false;
        }
        int numCount = 0;
        for (char c : voucher.toCharArray()) {
            if (Character.isDigit(c)) numCount++;
        }
        return numCount == 8;
    }
}