package com.store.repositories;

import com.store.models.Payments;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Thêm thanh toán mới
    public void addPayment(Payments payment) {
        String sql = "INSERT INTO payments (order_id, payment_date, amount, method) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                payment.getOrderId(),
                Timestamp.valueOf(payment.getPaymentDate()),
                payment.getAmount(),
                payment.getMethod());
    }

    // Lấy danh sách các thanh toán
    public List<Payments> getAllPayments() {
        String sql = "SELECT * FROM payments";
        return jdbcTemplate.query(sql, new PaymentRowMapper());
    }

    // Lấy thanh toán theo ID
    public Payments getPaymentById(int paymentId) {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        return jdbcTemplate.queryForObject(sql, new PaymentRowMapper(), paymentId);
    }

    // Xoá thanh toán theo ID
    public void deletePayment(int paymentId) {
        String sql = "DELETE FROM payments WHERE payment_id = ?";
        jdbcTemplate.update(sql, paymentId);
    }

    // RowMapper ánh xạ từ ResultSet sang Payments
    private static class PaymentRowMapper implements RowMapper<Payments> {
        @Override
        public Payments mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return new Payments(
                    rs.getInt("payment_id"),
                    rs.getInt("order_id"),
                    rs.getTimestamp("payment_date").toLocalDateTime(),
                    rs.getBigDecimal("amount"),
                    rs.getString("method"));
        }
    }
}
