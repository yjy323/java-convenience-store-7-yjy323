package store.service;

import store.dto.PaymentDto;
import store.model.Payment;

public class PaymentService {

    private final Payment payment;

    public PaymentService(Payment payment) {
        this.payment = payment;
    }

    public void confirmMembership(boolean confirm) {
        if (confirm) {
            payment.applyMembership();
        }
    }

    public PaymentDto paymentProcess() {
        return payment.createPaymentDto();
    }
}
