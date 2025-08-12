package com.store.services;

import com.store.models.Payments;
import com.store.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Thêm thanh toán mới
    public void addPayment(Payments payment) {
        validatePayment(payment); // Kiểm tra dữ liệu trước khi thêm
        paymentRepository.addPayment(payment);
    }

    // Lấy danh sách thanh toán
    public List<Payments> getAllPayments() {
        return paymentRepository.getAllPayments();
    }

    // Lấy thanh toán theo ID
    public Payments getPaymentById(int paymentId) {
        Payments payment = paymentRepository.getPaymentById(paymentId);
        if (payment == null) {
            throw new IllegalArgumentException("Không tìm thấy thanh toán với ID: " + paymentId);
        }
        return payment;
    }

    // Xoá thanh toán theo ID
    public void deletePayment(int paymentId) {
        Payments payment = paymentRepository.getPaymentById(paymentId);
        if (payment == null) {
            throw new IllegalArgumentException("Không thể xóa, thanh toán với ID: " + paymentId + " không tồn tại.");
        }
        paymentRepository.deletePayment(paymentId);
    }

    // Logic kiểm tra dữ liệu thanh toán
    private void validatePayment(Payments payment) {
        if (payment.getAmount() == null || payment.getAmount().doubleValue() <= 0) {
            throw new IllegalArgumentException("Số tiền thanh toán phải lớn hơn 0.");
        }
        if (payment.getPaymentDate() == null) {
            throw new IllegalArgumentException("Ngày thanh toán không được để trống.");
        }
        if (payment.getMethod() == null || payment.getMethod().isEmpty()) {
            throw new IllegalArgumentException("Phương thức thanh toán không được để trống.");
        }
    }
}
