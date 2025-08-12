package com.store.repositories;

import com.store.models.Inventory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class InventoryRepository {

    private final JdbcTemplate jdbcTemplate;

    // Inject JdbcTemplate vào repository
    public InventoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Lấy danh sách kho
    public List<Inventory> getAllInventories() {
        String query = "SELECT * FROM inventory";
        return jdbcTemplate.query(query, new InventoryRowMapper());
    }

    // Lấy thông tin inventory theo product_id
    public Inventory getInventoryByProductId(int productId) {
        String query = "SELECT * FROM inventory WHERE product_id = ?";
        return jdbcTemplate.queryForObject(query, new InventoryRowMapper(), productId);
    }

    // Cập nhật inventory
    public void updateInventory(Inventory inventory) {
        String sql = "UPDATE inventory SET quantity = ?, inbound = ?, outbound = ?, last_updated = ? WHERE product_id = ?";
        jdbcTemplate.update(sql, inventory.getQuantity(), inventory.getInbound(), inventory.getOutbound(),
                Timestamp.valueOf(inventory.getLastUpdated()), inventory.getProductId());
    }

    // Thêm mới inventory
    public void addInventory(Inventory inventory) {
        String sql = "INSERT INTO inventory (product_id, quantity, inbound, outbound, last_updated) VALUES (?, ?, ?, ?, ?)";
        try {
            Timestamp lastUdated = inventory.getLastUpdated() != null ? Timestamp.valueOf(inventory.getLastUpdated())
                    : Timestamp.valueOf(LocalDateTime.now());
            jdbcTemplate.update(sql, inventory.getProductId(), inventory.getQuantity(), inventory.getInbound(),
                    inventory.getOutbound(), lastUdated);
        } catch (Exception e) {
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class InventoryRowMapper implements RowMapper<Inventory> {
        @Override
        public Inventory mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return new Inventory(
                    rs.getInt("inventory_id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getInt("inbound"),
                    rs.getInt("outbound"),
                    rs.getTimestamp("last_updated").toLocalDateTime());
        }
    }
}
