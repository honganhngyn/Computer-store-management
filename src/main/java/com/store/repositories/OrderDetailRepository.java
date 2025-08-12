package com.store.repositories;

import com.store.models.OrderDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderDetailRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderDetailRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Thêm chi tiết đơn hàng
    public void addOrderDetail(OrderDetails orderDetail) {
        String sql = "INSERT INTO order_details (order_detail_id, order_id, product_id, quantity, price) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                orderDetail.getOrderDetailId(),
                orderDetail.getOrderId(),
                orderDetail.getProductId(),
                orderDetail.getQuantity(),
                orderDetail.getPrice());
    }

    // Liệt kê chi tiết đơn hàng theo ID đơn hàng
    public List<OrderDetails> getOrderDetailsByOrderId(int orderId) {
        String sql = "SELECT * FROM order_details WHERE order_id = ?";
        return jdbcTemplate.query(sql, new OrderDetailRowMapper(), orderId);
    }

    // Lấy tổng tiền của một đơn hàng
    public double getTotalAmountByOrderId(int orderId) {
        String sql = "SELECT SUM(quantity * price) AS total_amount FROM order_details WHERE order_id = ?";
        Double totalAmount = jdbcTemplate.queryForObject(sql, Double.class, orderId);
        return totalAmount != null ? totalAmount : 0.0;
    }

    // Truy vấn tổng số lượng sản phẩm bán ra
    public int getTotalSales() {
        String sql = "SELECT SUM(quantity) FROM order_details";
        Integer totalSales = jdbcTemplate.queryForObject(sql, Integer.class);
        return totalSales != null ? totalSales : 0;
    }

    // Truy vấn tổng doanh thu
    public double getTotalRevenue() {
        String sql = "SELECT SUM(quantity * price) FROM order_details";
        Double totalRevenue = jdbcTemplate.queryForObject(sql, Double.class);
        return totalRevenue != null ? totalRevenue : 0.0;
    }

    // RowMapper để ánh xạ ResultSet sang OrderDetails
    private static class OrderDetailRowMapper implements RowMapper<OrderDetails> {
        @Override
        public OrderDetails mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return new OrderDetails(
                    rs.getInt("order_detail_id"),
                    rs.getInt("order_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"));
        }
    }
}
