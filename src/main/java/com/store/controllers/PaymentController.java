package com.store.controllers;

import com.store.models.Payments;
import com.store.services.PaymentService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payments")
    public void addPayment(int paymentId, int orderId, BigDecimal amount, String method) {
        Payments payment = new Payments(paymentId, orderId, LocalDateTime.now(), amount, method);
        paymentService.addPayment(payment);
    }

    public void listAllPayments() {
        List<Payments> payments = paymentService.getAllPayments();
        if (payments != null) {
            for (Payments payment : payments) {
                System.out.println("ID: " + payment.getPaymentId() +
                        ", Order ID: " + payment.getOrderId() +
                        ", Date: " + payment.getPaymentDate() +
                        ", Amount: " + payment.getAmount() +
                        ", Method: " + payment.getMethod());
            }
        }
    }

    public void getPaymentById(int paymentId) {
        Payments payment = paymentService.getPaymentById(paymentId);
        if (payment != null) {
            System.out.println("ID: " + payment.getPaymentId() +
                    ", Order ID: " + payment.getOrderId() +
                    ", Date: " + payment.getPaymentDate() +
                    ", Amount: " + payment.getAmount() +
                    ", Method: " + payment.getMethod());
        } else {
            System.out.println("Không tìm thấy thanh toán.");
        }
    }

    public void deletePayment(int paymentId) {
        paymentService.deletePayment(paymentId);
    }
}
