package com.store.controllers;

import com.store.models.Suppliers;
import com.store.services.SupplierService;

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

@Controller
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/suppliers")
    public String getSuppliers(Model model) {
        List<Suppliers> suppliers = supplierService.getAllSuppliers();

        if (suppliers == null || suppliers.isEmpty()) {
            model.addAttribute("message", "Không có nhà cung cấp nào!");
            suppliers = new ArrayList<>(); // Khởi tạo danh sách trống để tránh lỗi.
        }

        model.addAttribute("suppliers", suppliers);
        model.addAttribute("newSupplier", new Suppliers()); // Gửi đối tượng rỗng cho form
        return "suppliers";
    }

    // Thêm nhà cung cấp mới
    @PostMapping("/suppliers/add")
    @ResponseBody
    public Map<String, String> addSupplier(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address) {
        Map<String, String> response = new HashMap<>();
        try {
            Suppliers newSupplier = new Suppliers();
            newSupplier.setName(name);
            newSupplier.setEmail(email);
            newSupplier.setPhone(phone);
            newSupplier.setAddress(address);

            supplierService.addSupplier(newSupplier);
            response.put("message", "Thông tin nhà cung cấp đã được thêm thành công!");
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    // Câp nhật nhà cung cấp
    public void updateSupplier(Suppliers supplier) {
        supplierService.updateSupplier(supplier);
    }

    // Tìm kiếm sản phẩm theo từ khoá
    @GetMapping("/suppliers/search")
    public ResponseEntity<List<Suppliers>> searchSupplier(@RequestParam String keyword) {
        List<Suppliers> suppliers = supplierService.searchSuppliers(keyword);

        return ResponseEntity.ok(suppliers);
    }

    @PostMapping("/suppliers/update")
    @ResponseBody
    public Map<String, String> updateSupplier(
            @RequestParam("id") int supplierId,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address) {
        Map<String, String> response = new HashMap<>();
        try {
            Suppliers updatedSupplier = new Suppliers(supplierId, name, email, phone, address);
            supplierService.updateSupplier(updatedSupplier);
            response.put("message", "Nhà cung cấp đã được cập nhật thành công!");
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    // Xoá nhà cung cấp theo ID
    public void deleteSupplier(int supplierId) {
        supplierService.deleteSupplier(supplierId);
    }
}
