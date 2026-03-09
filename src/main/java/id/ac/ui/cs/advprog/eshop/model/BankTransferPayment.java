package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

public class BankTransferPayment extends Payment {

    public BankTransferPayment(String id, Map<String, String> paymentData) {
        super(id, "BANK_TRANSFER", paymentData);
        this.status = validateBankTransfer(paymentData) ? "SUCCESS" : "REJECTED";
    }

    private boolean validateBankTransfer(Map<String, String> data) {
        String bankName = data.get("bankName");
        String refCode = data.get("referenceCode");
        return bankName != null && !bankName.trim().isEmpty() &&
                refCode != null && !refCode.trim().isEmpty();
    }
}