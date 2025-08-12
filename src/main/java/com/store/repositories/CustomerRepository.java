package com.store.repositories;

import com.store.models.Customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Lấy danh sách tất cả khách hàng
    public List<Customer> getAllCustomers() {
        String query = "SELECT * FROM customers";
        return jdbcTemplate.query(query, new CustomerRowMapper());
    }

    // Thêm khách hàng mới
    public void addCustomer(Customer customer) {
        String query = "INSERT INTO customers (name, email, phone, address, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            Timestamp createdAt = customer.getCreatedAt() != null ? Timestamp.valueOf(customer.getCreatedAt())
                    : Timestamp.valueOf(LocalDateTime.now());
            jdbcTemplate.update(query, customer.getName(), customer.getEmail(),
                    customer.getPhone(), customer.getAddress(), createdAt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cập nhật thông tin khách hàng
    public void updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET name = ?, email = ?, phone = ?, address = ?, created_at = ? WHERE customer_id = ?";
        jdbcTemplate.update(sql,
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                Timestamp.valueOf(customer.getCreatedAt()),
                customer.getCustomerId());
    }

    // Tìm khách hàng theo ID, tên hoặc số điện thoại
    public List<Customer> searchCustomers(String keyword) {
        String query = """
                    SELECT * FROM customers
                    WHERE CAST(customer_id AS VARCHAR) LIKE ? OR
                          name LIKE ? OR
                          phone LIKE ?
                """;
        String formattedKeyword = "%" + keyword + "%";
        return jdbcTemplate.query(query, new CustomerRowMapper(), formattedKeyword, formattedKeyword, formattedKeyword);
    }

    // Tìm khách hàng theo ID
    public Customer getCustomerById(int customerId) {
        String query = "SELECT * FROM customers WHERE customer_id = ?";
        return jdbcTemplate.queryForObject(query, new CustomerRowMapper(), customerId);
    }

    private static class CustomerRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getTimestamp("created_at").toLocalDateTime());
        }
    }
}
