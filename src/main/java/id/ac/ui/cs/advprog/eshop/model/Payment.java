package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;

        if ("VOUCHER".equals(method)) {
            this.status = validateVoucher(paymentData.get("voucherCode")) ? "SUCCESS" : "REJECTED";
        } else if ("BANK_TRANSFER".equals(method)) {
            this.status = validateBankTransfer(paymentData) ? "SUCCESS" : "REJECTED";
        } else {
            this.status = "REJECTED";
        }
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

    private boolean validateBankTransfer(Map<String, String> data) {
        String bankName = data.get("bankName");
        String refCode = data.get("referenceCode");
        return bankName != null && !bankName.trim().isEmpty() && refCode != null && !refCode.trim().isEmpty();
    }
}
