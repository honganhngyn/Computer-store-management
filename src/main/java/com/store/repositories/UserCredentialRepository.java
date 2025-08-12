package com.store.repositories;

import com.store.models.UserCredential;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserCredentialRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserCredentialRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Tìm tài khoản theo username
    public Optional<UserCredential> findByUsername(String username) {
        String query = "SELECT * FROM user_credentials WHERE username = ?";
        return jdbcTemplate.query(query, new UserCredentialRowMapper(), username)
                .stream()
                .findFirst();
    }

    // Thêm tài khoản mới
    public void save(UserCredential userCredential) {
        String query = "INSERT INTO user_credentials (username, password, role) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, userCredential.getUsername(), userCredential.getPassword(),
                userCredential.getRole());
    }

    private static class UserCredentialRowMapper implements RowMapper<UserCredential> {
        @Override
        public UserCredential mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return new UserCredential(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"));
        }
    }
}