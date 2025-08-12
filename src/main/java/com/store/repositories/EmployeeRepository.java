package com.store.repositories;

import com.store.models.Employees;
//import com.store.utils.PasswordUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class EmployeeRepository {

        private final JdbcTemplate jdbcTemplate;

        public EmployeeRepository(JdbcTemplate jdbcTemplate) {
                this.jdbcTemplate = jdbcTemplate;
        }

        // Lấy danh sách nhân viên (ẩn mật khẩu)
        public List<Employees> getAllEmployees() {
                String sql = "SELECT employee_id, name, username, is_admin, position, salary, email, phone, hired_date FROM employees";
                return jdbcTemplate.query(sql, new EmployeeRowMapper());
        }

        // Thêm nhân viên (mã hóa mật khẩu trước khi lưu)
        public void addEmployee(Employees employee) {
                String sql = "INSERT INTO employees (name, username, is_admin, position, salary, email, phone, hired_date) "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                // String encryptedPassword =
                // PasswordUtils.encryptPassword(employee.getPassword());
                jdbcTemplate.update(sql, employee.getName(), employee.getUsername(),
                                employee.isAdmin(), employee.getPosition(), employee.getSalary(), employee.getEmail(),
                                employee.getPhone(), Timestamp.valueOf(employee.getHiredDate()));
        }

        // Cập nhật nhân viên (mã hóa mật khẩu nếu thay đổi)
        public void updateEmployee(Employees employee) {
                String sql = """
                                UPDATE employees
                                SET name = ?, username = ?, is_admin = ?,
                                    position = ?, salary = ?,
                                    email = ?, phone = ?, hired_date = ?
                                WHERE employee_id = ?
                                """;
                jdbcTemplate.update(sql, employee.getName(), employee.getUsername(),
                                employee.getPosition(), employee.getSalary(),
                                employee.getEmail(),
                                employee.getPhone(), Timestamp.valueOf(employee.getHiredDate()),
                                employee.getEmployeeId());

                // if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
                // // Cập nhật mật khẩu nếu được cung cấp
                // sql = """
                // UPDATE employees
                // SET name = ?, username = ?, password = ?, is_admin = ?,
                // position = ?, salary = ?,
                // email = ?, phone = ?, hired_date = ?
                // WHERE employee_id = ?
                // """;
                // String encryptedPassword =
                // PasswordUtils.encryptPassword(employee.getPassword());
                // jdbcTemplate.update(sql, employee.getName(), employee.getUsername(),
                // encryptedPassword,
                // employee.getPosition(), employee.isAdmin(), employee.getSalary(),
                // employee.getEmail(),
                // employee.getPhone(), Timestamp.valueOf(employee.getHiredDate()),
                // employee.getEmployeeId());
                // } else {
                // // Không cập nhật mật khẩu
                // jdbcTemplate.update(sql, employee.getName(), employee.getUsername(),
                // employee.getPosition(), employee.isAdmin(), employee.getSalary(),
                // employee.getEmail(),
                // employee.getPhone(), Timestamp.valueOf(employee.getHiredDate()),
                // employee.getEmployeeId());
                // }
        }

        // Tìm nhân viên theo ID, tên, username hoặc số điện thoại
        public List<Employees> searchEmployees(String keyword) {
                String query = """
                                    SELECT * FROM employees
                                    WHERE CAST(employee_id AS VARCHAR) LIKE ? OR
                                          name LIKE ? OR
                                          username LIKE ? OR
                                          phone LIKE ?
                                """;
                String formattedKeyword = "%" + keyword + "%";
                return jdbcTemplate.query(query, new EmployeeRowMapper(), formattedKeyword, formattedKeyword,
                                formattedKeyword, formattedKeyword);
        }

        // Lấy nhân viên theo username
        public Employees getEmployeeByUsername(String username) {
                String sql = "SELECT * FROM employees WHERE username = ?";
                return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), username);
        }

        // Lấy nhân viên theo ID
        public Employees getEmployeeById(int employeeId) {
                String sql = "SELECT employee_id, name, username, is_admin, position, salary, email, phone, hired_date FROM employees WHERE employee_id = ?";
                return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), employeeId);
        }

        private static class EmployeeRowMapper implements RowMapper<Employees> {
                // private final boolean includePassword;

                // public EmployeeRowMapper() {
                // this(false); // Mặc định không bao gồm mật khẩu
                // }

                // public EmployeeRowMapper(boolean includePassword) {
                // this.includePassword = includePassword;
                // }

                @Override
                public Employees mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
                        Employees employee = new Employees(
                                        rs.getInt("employee_id"),
                                        rs.getString("name"),
                                        rs.getString("username"),
                                        rs.getBoolean("is_admin"),
                                        rs.getString("position"),
                                        rs.getDouble("salary"),
                                        rs.getString("email"),
                                        rs.getString("phone"),
                                        rs.getTimestamp("hired_date").toLocalDateTime());

                        // // Bao gồm mật khẩu nếu được yêu cầu (dành cho xác thực)
                        // if (includePassword) {
                        // employee.setPassword(rs.getString("password"));
                        // }

                        return employee;
                }
        }
}