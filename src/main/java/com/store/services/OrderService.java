package com.store.services;

import com.store.models.Orders;
import com.store.models.OrderDetails;
import com.store.repositories.OrderDetailRepository;
import com.store.repositories.OrderRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    // Lấy danh sách đơn hàng
    public List<Orders> getAllOrders() {
        return orderRepository.getOrders();
    }

    // Lấy đơn hàng theo ID
    public Orders getOrderById(int orderId) {
        return orderRepository.getOrderById(orderId);
    }

    // Tạo đơn hàng mới
    public void createOrder(Orders order, List<OrderDetails> orderDetails) {
        validateOrder(order);
        validateOrderDetails(orderDetails);

        // Đặt trạng thái mặc định cho đơn hàng
        order.setStatus("Pending");

        // Thêm đơn hàng và chi tiết đơn hàng
        try {
            orderRepository.addOrder(order);

            for (OrderDetails detail : orderDetails) {
                detail.setOrderId(order.getOrderId());
                orderDetailRepository.addOrderDetail(detail);
            }

            System.out.println("Đơn hàng được tạo thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi tạo đơn hàng: " + e.getMessage());
        }
    }

    // Lấy tổng tiền của đơn hàng
    public double getOrderTotalAmount(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("ID đơn hàng không hợp lệ.");
        }
        try {
            return orderDetailRepository.getTotalAmountByOrderId(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi tính tổng tiền đơn hàng: " + e.getMessage());
        }
    }

    // Cập nhật trạng thái đơn hàng
    public void updateOrderStatus(int orderId, String status) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("ID đơn hàng không hợp lệ.");
        }
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Trạng thái đơn hàng không được để trống.");
        }

        try {
            Orders existingOrder = orderRepository.getOrderById(orderId);
            if (existingOrder == null) {
                throw new IllegalArgumentException("Không tìm thấy đơn hàng với ID: " + orderId);
            }

            orderRepository.updateOrderStatus(orderId, status);
            System.out.println("Cập nhật trạng thái đơn hàng thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật trạng thái đơn hàng: " + e.getMessage());
        }
    }

    // Lấy tổng số lượng sản phẩm bán ra
    public int getTotalSales() {
        return orderDetailRepository.getTotalSales();
    }

    // Lấy tổng doanh thu từ tất cả đơn hàng
    public double getTotalRevenue() {
        return orderDetailRepository.getTotalRevenue();
    }

    // Kiểm tra tính hợp lệ của đơn hàng
    private void validateOrder(Orders order) {
        if (order == null) {
            throw new IllegalArgumentException("Đơn hàng không được null.");
        }
        if (order.getCustomerId() <= 0) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ.");
        }
        if (order.getEmployeeId() <= 0) {
            throw new IllegalArgumentException("ID nhân viên không hợp lệ.");
        }
    }

    // Kiểm tra tính hợp lệ của danh sách chi tiết đơn hàng
    private void validateOrderDetails(List<OrderDetails> orderDetails) {
        if (orderDetails == null || orderDetails.isEmpty()) {
            throw new IllegalArgumentException("Danh sách chi tiết đơn hàng không được rỗng.");
        }

        for (OrderDetails detail : orderDetails) {
            if (detail.getProductId() <= 0) {
                throw new IllegalArgumentException("ID sản phẩm không hợp lệ trong chi tiết đơn hàng.");
            }
            if (detail.getQuantity() <= 0) {
                throw new IllegalArgumentException("Số lượng sản phẩm phải lớn hơn 0.");
            }
            if (detail.getPrice() < 0) {
                throw new IllegalArgumentException("Giá sản phẩm không được âm.");
            }
        }
    }
}
