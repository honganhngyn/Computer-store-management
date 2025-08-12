package com.store.services;

import com.store.models.Inventory;
import com.store.repositories.InventoryRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    // Lấy danh sách kho
    public List<Inventory> getAllInventories() {
        List<Inventory> inventories = inventoryRepository.getAllInventories();
        if (inventories == null) {
            return List.of();
        }
        return inventories;
    }

    // Thêm mới inventory
    public void addInventory(Inventory inventory) {
        validateInventory(inventory);
        inventoryRepository.addInventory(inventory);
    }

    // Tìm thông tin inventory theo product_id
    public Inventory getInventoryByProductId(int productId) {
        Inventory inventory = inventoryRepository.getInventoryByProductId(productId);
        if (inventory == null) {
            throw new IllegalArgumentException("Không tìm thấy kho với product_id: " + productId);
        }
        return inventory;
    }

    // Cập nhật inventory
    public void updateInventory(Inventory inventory) {
        validateInventory(inventory);
        Inventory existingInventory = getInventoryByProductId(inventory.getProductId());
        if (existingInventory == null) {
            throw new IllegalArgumentException("Không tìm thấy kho với product_id: " + inventory.getProductId());
        }
        inventoryRepository.updateInventory(inventory);
    }

    // Nhập kho: tăng số lượng
    public void inboundProduct(int productId, int amount) {
        try {
            Inventory inventory = getInventoryByProductId(productId);
            inventory.setInbound(inventory.getInbound() + amount);
            inventory.setQuantity(inventory.getQuantity() + amount);
            inventory.setLastUpdated(java.time.LocalDateTime.now());
            inventoryRepository.updateInventory(inventory);
            System.out.println("Nhập kho thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi nhập kho: " + e.getMessage());
        }
    }

    // Xuất kho: giảm số lượng
    public void outboundProduct(int productId, int amount) {
        try {
            Inventory inventory = getInventoryByProductId(productId);
            if (inventory.getQuantity() < amount) {
                throw new IllegalArgumentException("Không đủ hàng tồn kho để xuất.");
            }
            inventory.setOutbound(inventory.getOutbound() + amount);
            inventory.setQuantity(inventory.getQuantity() - amount);
            inventory.setLastUpdated(java.time.LocalDateTime.now());
            inventoryRepository.updateInventory(inventory);
            System.out.println("Xuất kho thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi xuất kho: " + e.getMessage());
        }
    }

    // Đồng bộ với sản phẩm khi cập nhật số lượng
    public void syncInventoryWithProduct(int productId, int newQuantity) {
        try {
            Inventory inventory = getInventoryByProductId(productId);
            int quantityChange = newQuantity - inventory.getQuantity();
            if (quantityChange > 0) {
                inboundProduct(productId, quantityChange);
            } else if (quantityChange < 0) {
                outboundProduct(productId, -quantityChange);
            }
            System.out.println("Đồng bộ kho với sản phẩm thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi đồng bộ kho: " + e.getMessage());
        }
    }

    // Logic kiểm tra dữ liệu inventory
    private void validateInventory(Inventory inventory) {
        if (inventory.getQuantity() < 0) {
            throw new IllegalArgumentException("Số lượng không thể âm.");
        }
        if (inventory.getInbound() < 0) {
            throw new IllegalArgumentException("Số lượng nhập không thể âm.");
        }
        if (inventory.getOutbound() < 0) {
            throw new IllegalArgumentException("Số lượng xuất không thể âm.");
        }
    }
}
