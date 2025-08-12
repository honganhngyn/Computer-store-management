package com.store.controllers;

import com.store.models.Customer;
import com.store.services.CustomerService;
import com.store.utils.FormatUtils;

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

@Controller
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public String getCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();

        if (customers == null || customers.isEmpty()) {
            model.addAttribute("message", "Không có khách hàng nào!");
            customers = new ArrayList<>(); // Khởi tạo danh sách trống để tránh lỗi.
        }

        // Định dạng dữ liệu
        customers.forEach(customer -> {
            customer.setFormattedDate(FormatUtils.formatDateTime(customer.getCreatedAt()));
        });

        model.addAttribute("customers", customers);
        model.addAttribute("newCustomer", new Customer()); // Gửi đối tượng rỗng cho form
        return "customer";
    }

    // Thêm khách hàng mới
    @PostMapping("/customers/add")
    @ResponseBody
    public Map<String, String> addCustomer(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address) {
        Map<String, String> response = new HashMap<>();
        try {
            Customer newCustomer = new Customer();
            newCustomer.setName(name);
            newCustomer.setEmail(email);
            newCustomer.setPhone(phone);
            newCustomer.setAddress(address);
            newCustomer.setCreatedAt(LocalDateTime.now());

            customerService.addCustomer(newCustomer);
            response.put("message", "Thông tin khách hàng đã được thêm thành công!");
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    // Tìm kiếm sản phẩm theo từ khoá
    @GetMapping("/customers/search")
    public ResponseEntity<List<Customer>> searchCustomers(@RequestParam String keyword) {
        List<Customer> customers = customerService.searchCustomers(keyword);

        // Định dạng dữ liệu
        customers.forEach(customer -> {
            customer.setFormattedDate(FormatUtils.formatDateTime(customer.getCreatedAt()));
        });

        return ResponseEntity.ok(customers);
    }

    @PostMapping("/customers/update")
    @ResponseBody
    public Map<String, String> updateCustomer(
            @RequestParam("id") int customerId,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("createdAt") String createdAtStr) {
        Map<String, String> response = new HashMap<>();
        try {
            LocalDateTime createdAt = LocalDateTime.parse(createdAtStr);
            Customer updatedCustomer = new Customer(customerId, name, email, phone, address,
                    createdAt);

            customerService.updateCustomer(updatedCustomer);
            response.put("message", "Khách hàng đã được cập nhật thành công!");
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }
}
