package com.store.services;

import com.store.models.Customer;
import com.store.repositories.CustomerRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Lấy danh sách khách hàng
    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.getAllCustomers();
        if (customers == null) {
            return List.of();
        }
        return customers;
    }

    // Thêm khách hàng
    public void addCustomer(Customer customer) {
        validateCustomer(customer);
        customerRepository.addCustomer(customer);
    }

    // Cập nhật thông tin khách hàng
    public void updateCustomer(Customer customer) {
        validateCustomer(customer);
        Customer existingCustomer = customerRepository.getCustomerById(customer.getCustomerId());
        if (existingCustomer == null) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + customer.getCustomerId());
        }
        customerRepository.updateCustomer(customer);
    }

    // Tìm kiếm sản phẩm theo từ khóa
    public List<Customer> searchCustomers(String keyword) {
        return customerRepository.searchCustomers(keyword);
    }

    // Tìm khách hàng theo ID
    public Customer getCustomerById(int customerId) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + customerId);
        }
        return customer;
    }

    // Logic kiểm tra dữ liệu khách hàng
    private void validateCustomer(Customer customer) {
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống.");
        }
        if (customer.getEmail() == null || !customer.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Email khách hàng không hợp lệ.");
        }
        if (customer.getPhone() == null || !customer.getPhone().matches("\\d{10,15}")) {
            throw new IllegalArgumentException("Số điện thoại khách hàng không hợp lệ.");
        }
        if (customer.getAddress() == null || customer.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ khách hàng không được để trống.");
        }
    }
}
