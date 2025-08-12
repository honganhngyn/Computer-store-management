package com.store.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.store.models.Inventory;
import com.store.services.InventoryService;
import com.store.utils.FormatUtils;

@Controller
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory")
    public String getInventory(Model model) {
        List<Inventory> inventories = inventoryService.getAllInventories();

        if (inventories == null || inventories.isEmpty()) {
            model.addAttribute("message", "Không có sản phẩm nào!");
            inventories = new ArrayList<>(); // Khởi tạo danh sách trống để tránh lỗi.
        }

        // Định dạng dữ liệu
        inventories.forEach(inventory -> {
            inventory.setFormattedDate(FormatUtils.formatDateTime(inventory.getLastUpdated()));
        });

        model.addAttribute("inventories", inventories);
        model.addAttribute("newInventory", new Inventory()); // Gửi đối tượng rỗng cho form
        return "inventory";
    }

    // Thêm mới inventory
    @PostMapping("/inventory/add")
    @ResponseBody
    public Map<String, String> addInventory(
            @RequestParam Integer productId, @RequestParam Integer quantity,
            @RequestParam Integer inbound, @RequestParam Integer outbound) {
        Map<String, String> response = new HashMap<>();
        try {
            Inventory newInventory = new Inventory();
            newInventory.setProductId(productId);
            newInventory.setQuantity(quantity);
            newInventory.setInbound(inbound);
            newInventory.setOutbound(outbound);
            newInventory.setLastUpdated(LocalDateTime.now());
            inventoryService.addInventory(newInventory);
            response.put("message", "Thêm kho thành công.");
        } catch (Exception e) {
            response.put("message", "Lỗi: " + e.getMessage());
        }
        return response;
    }

    // Tìm kiếm kho theo product_id
    @GetMapping("/inventory/search")
    public ResponseEntity<Inventory> searchInventory(@RequestParam Integer productId) {
        Inventory inventory = inventoryService.getInventoryByProductId(productId);

        // Định dạng lại ngày tháng
        if (inventory != null) {
            inventory.setFormattedDate(FormatUtils.formatDateTime(inventory.getLastUpdated()));
        }

        return ResponseEntity.ok(inventory);
    }

    // Cập nhật inventory
    @PostMapping("/inventory/update")
    @ResponseBody
    public Map<String, String> updateInventory(
            @RequestParam("id") int inventoryId, @RequestParam Integer productId, @RequestParam Integer quantity,
            @RequestParam Integer inbound, @RequestParam Integer outbound,
            @RequestParam String lastUpdatedStr) {
        Map<String, String> response = new HashMap<>();
        try {
            LocalDateTime lastUpdated = LocalDateTime.parse(lastUpdatedStr);
            Inventory updatedInventory = new Inventory(inventoryId, productId, quantity, inbound, outbound,
                    lastUpdated);
            inventoryService.updateInventory(updatedInventory);
            response.put("message", "Cập nhật kho thành công.");
        } catch (Exception e) {
            response.put("error", "Lỗi khi cập nhật kho: " + e.getMessage());
        }
        return response;
    }

    public void inboundProduct(Integer productId, int amount) {
        try {
            inventoryService.inboundProduct(productId, amount);
            System.out.println("Nhập kho thành công.");
        } catch (Exception e) {
            System.err.println("Lỗi nhập kho: " + e.getMessage());
        }
    }

    public void outboundProduct(Integer productId, int amount) {
        try {
            inventoryService.outboundProduct(productId, amount);
            System.out.println("Xuất kho thành công.");
        } catch (Exception e) {
            System.err.println("Lỗi xuất kho: " + e.getMessage());
        }
    }

    public void syncWithProduct(Integer productId, int newQuantity) {
        try {
            inventoryService.syncInventoryWithProduct(productId, newQuantity);
            System.out.println("Đồng bộ kho thành công.");
        } catch (Exception e) {
            System.err.println("Lỗi đồng bộ kho: " + e.getMessage());
        }
    }
}
