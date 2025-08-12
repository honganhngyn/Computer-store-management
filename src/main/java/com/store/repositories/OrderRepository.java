package com.store.repositories;

import com.store.models.Orders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Liệt kê danh sách đơn hàng
    public List<Orders> getOrders() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    // Thêm đơn hàng
    public void addOrder(Orders order) {
        String sql = "INSERT INTO orders (customer_id, employee_id, order_date, status) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                order.getCustomerId(),
                order.getEmployeeId(),
                new Date(order.getOrderDate().getTime()),
                order.getStatus());
    }

    // Cập nhật trạng thái đơn hàng
    public void updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        jdbcTemplate.update(sql, status, orderId);
    }

    // Tìm kiếm đơn hàng theo ID
    public Orders getOrderById(int orderId) {
        String query = "SELECT * FROM orders WHERE order_id = ?";
        return jdbcTemplate.queryForObject(query, new OrderRowMapper(), orderId);
    }

    // RowMapper ánh xạ ResultSet sang Orders
    private static class OrderRowMapper implements RowMapper<Orders> {
        @Override
        public Orders mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return new Orders(
                    rs.getInt("order_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("employee_id"),
                    rs.getDate("order_date"),
                    rs.getString("status"));
        }
    }
}
