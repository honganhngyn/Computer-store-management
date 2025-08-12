package com.store.models;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "customer_id", nullable = false)
    private int customerId;

    @Column(name = "employee_id", nullable = false)
    private int employeeId;

    @Column(name = "order_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Transient
    private String formattedDate;

    public Orders() {
    }

    public Orders(int orderId, int customerId, int employeeId, Date orderDate, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.orderDate = orderDate;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", customerId=" + customerId + ", employeeId=" + employeeId
                + ", orderDate=" + orderDate + ", status=" + status + "]";
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}
