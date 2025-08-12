package com.store.repositories;

import com.store.models.Suppliers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;

@Repository
public class SupplierRepository {

    private final JdbcTemplate jdbcTemplate;

    public SupplierRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Lấy danh sách nhà cung cấp
    public List<Suppliers> getAllSuppliers() {
        String sql = "SELECT * FROM suppliers";
        return jdbcTemplate.query(sql, new SupplierRowMapper());
    }

    // Thêm nhà cung cấp mới
    public void addSupplier(Suppliers supplier) {
        String sql = "INSERT INTO suppliers (name, email, phone, address) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getAddress());
    }

    // Cập nhật thông tin nhà cung cấp
    public void updateSupplier(Suppliers supplier) {
        String sql = "UPDATE suppliers SET name = ?, email = ?, phone = ?, address = ? WHERE supplier_id = ?";
        jdbcTemplate.update(sql,
                supplier.getName(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getAddress(),
                supplier.getSupplierId());
    }

    // Xóa nhà cung cấp theo ID
    public void deleteSupplier(int supplierId) {
        String sql = "DELETE FROM suppliers WHERE supplier_id = ?";
        jdbcTemplate.update(sql, supplierId);
    }

    // Tìm nhà cung cấp theo ID, tên hoặc số điện thoại
    public List<Suppliers> searchSuppliers(String keyword) {
        String query = """
                    SELECT * FROM suppliers
                    WHERE CAST(supplier_id AS VARCHAR) LIKE ? OR
                          name LIKE ? OR
                          phone LIKE ?
                """;
        String formattedKeyword = "%" + keyword + "%";
        return jdbcTemplate.query(query, new SupplierRowMapper(), formattedKeyword, formattedKeyword, formattedKeyword);
    }

    // Tìm nhà cung cấp theo ID
    public Suppliers getSupplierById(int supplierId) {
        String query = "SELECT * FROM suppliers WHERE supplier_id = ?";
        return jdbcTemplate.queryForObject(query, new SupplierRowMapper(), supplierId);
    }

    // RowMapper để ánh xạ từ ResultSet sang đối tượng Suppliers
    private static class SupplierRowMapper implements RowMapper<Suppliers> {
        @Override
        public Suppliers mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return new Suppliers(
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"));
        }
    }
}
