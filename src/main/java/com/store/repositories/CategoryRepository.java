package com.store.repositories;

import com.store.models.Categories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Lấy tất cả danh mục
    public List<Categories> getAllCategories() {
        String query = "SELECT * FROM categories";
        return jdbcTemplate.query(query, new CategoryRowMapper());

    }

    // Tìm danh mục theo id hoặc tên
    public List<Categories> searchCategories(String keyword) {
        String query = """
                    SELECT * FROM categories
                    WHERE CAST(category_id AS VARCHAR) LIKE ? OR
                          name LIKE ?
                """;
        String formattedKeyword = "%" + keyword + "%";
        return jdbcTemplate.query(query, new CategoryRowMapper(), formattedKeyword, formattedKeyword);
    }

    // Thêm danh mục mới
    public void addCategory(Categories category) {
        String query = "INSERT INTO categories (name) VALUES (?)";
        jdbcTemplate.update(query, category.getName());
    }

    // RowMapper ánh xạ từ ResultSet sang Product
    private static class CategoryRowMapper implements RowMapper<Categories> {
        @Override
        public Categories mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return new Categories(
                    rs.getInt("category_id"),
                    rs.getString("name"));
        }
    }
}
