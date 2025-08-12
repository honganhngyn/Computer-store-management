package com.store.repositories;

import com.store.models.Product;
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
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Lấy danh sách sản phẩm
    public List<Product> getAllProducts() {
        String query = "SELECT * FROM products";
        return jdbcTemplate.query(query, new ProductRowMapper());
    }

    // Thêm sản phẩm mới
    public void addProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, category_id, supplier_id, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Timestamp createdAt = product.getCreatedAt() != null ? Timestamp.valueOf(product.getCreatedAt())
                    : Timestamp.valueOf(LocalDateTime.now());
            jdbcTemplate.update(sql,
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getCategoryId(),
                    product.getSupplierId(),
                    createdAt);
            System.out.println("Thêm sản phẩm thành công: " + product.getName());
        } catch (Exception e) {
            System.err.println("Lỗi thêm sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Cập nhật sản phẩm
    public void updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, category_id = ?, supplier_id = ?, created_at = ? WHERE product_id = ?";
        jdbcTemplate.update(sql,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategoryId(),
                product.getSupplierId(),
                Timestamp.valueOf(product.getCreatedAt()),
                product.getProductId());
    }

    // Tìm sản phẩm theo từ khóa
    public List<Product> searchProducts(String keyword) {
        String query = """
                    SELECT * FROM products
                    WHERE CAST(product_id AS VARCHAR) LIKE ? OR
                          name LIKE ? OR
                          description LIKE ?
                """;
        String formattedKeyword = "%" + keyword + "%";
        return jdbcTemplate.query(query, new ProductRowMapper(), formattedKeyword, formattedKeyword, formattedKeyword);
    }

    // Tìm sản phẩm theo ID
    public Product getProductById(int productId) {
        String query = "SELECT * FROM products WHERE product_id = ?";
        return jdbcTemplate.queryForObject(query, new ProductRowMapper(), productId);
    }

    // RowMapper ánh xạ từ ResultSet sang Product
    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return new Product(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("category_id"),
                    rs.getInt("supplier_id"),
                    rs.getTimestamp("created_at").toLocalDateTime());
        }
    }
}
