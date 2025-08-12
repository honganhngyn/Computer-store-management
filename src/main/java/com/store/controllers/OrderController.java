package com.store.controllers;

import com.store.models.Orders;
import com.store.models.OrderDetails;
import com.store.services.OrderService;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model) {
        List<Orders> orders = orderService.getAllOrders();

        if (orders == null || orders.isEmpty()) {
            model.addAttribute("message", "Không có đơn hàng nào!");
            orders = List.of(); // Khởi tạo danh sách trống để tránh lỗi.
        }

        // Định dạng dữ liệu
        orders.forEach(order -> {
            order.setFormattedDate(order.getOrderDate().toString());
        });

        model.addAttribute("orders", orders);
        return "orders";
    }

    @PostMapping("/orders/add")
    @ResponseBody
    public Map<String, String> addOrder(
            @RequestParam("customerId") int customerId,
            @RequestParam("employeeId") int employeeId,
            @RequestParam("orderDate") String orderDate,
            @RequestParam("status") String status,
            @RequestParam("orderDetails") List<OrderDetails> orderDetails) {
        Map<String, String> response = new HashMap<>();
        try {
            Orders newOrder = new Orders();
            newOrder.setCustomerId(customerId);
            newOrder.setEmployeeId(employeeId);
            newOrder.setOrderDate(java.sql.Timestamp.valueOf(orderDate));
            newOrder.setStatus(status);

            orderService.createOrder(newOrder, orderDetails);
            response.put("message", "Đơn hàng đã được thêm thành công!");
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/orders/search")
    @ResponseBody
    public ResponseEntity<Orders> getOrderById(@RequestParam int orderId) {
        Orders order = orderService.getOrderById(orderId);

        // Định dạng dữ liệu
        order.setFormattedDate(order.getOrderDate().toString());
        return ResponseEntity.ok(order);
    }

    @PostMapping("/orders/updateStatus")
    @ResponseBody
    public Map<String, String> updateOrderStatus(@RequestParam("orderId") int orderId,
            @RequestParam("status") String status) {
        Map<String, String> response = new HashMap<>();
        try {
            orderService.updateOrderStatus(orderId, status);
            response.put("message", "Trạng thái đơn hàng đã được cập nhật thành công!");
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/orders/totalAmount/{orderId}")
    @ResponseBody
    public ResponseEntity<Double> getOrderTotalAmount(@RequestParam int orderId) {
        double totalAmount = orderService.getOrderTotalAmount(orderId);
        return ResponseEntity.ok(totalAmount);
    }
}
