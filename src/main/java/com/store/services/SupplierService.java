package com.store.services;

import com.store.models.Suppliers;
import com.store.repositories.SupplierRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    // Lấy danh sách nhà cung cấp
    public List<Suppliers> getAllSuppliers() {
        List<Suppliers> suppliers = supplierRepository.getAllSuppliers();
        if (suppliers == null) {
            return List.of();
        }
        return suppliers;
    }

    // Thêm nhà cung cấp mới
    public void addSupplier(Suppliers supplier) {
        validateSupplier(supplier);
        supplierRepository.addSupplier(supplier);
    }

    // Cập nhật nhà cung cấp
    public void updateSupplier(Suppliers supplier) {
        validateSupplier(supplier);
        Suppliers existingSupplier = supplierRepository.getSupplierById(supplier.getSupplierId());
        if (existingSupplier == null) {
            throw new IllegalArgumentException("Nhà cung cấp không tồn tại với ID: " + supplier.getSupplierId());
        }
        supplierRepository.updateSupplier(supplier);
    }

    // Tìm kiếm sản phẩm theo từ khóa
    public List<Suppliers> searchSuppliers(String keyword) {
        return supplierRepository.searchSuppliers(keyword);
    }

    // Xoá nhà cung cấp theo ID
    public void deleteSupplier(int supplierId) {
        Suppliers existingSupplier = supplierRepository.getSupplierById(supplierId);
        if (existingSupplier == null) {
            throw new IllegalArgumentException("Nhà cung cấp không tồn tại với ID: " + supplierId);
        }
        supplierRepository.deleteSupplier(supplierId);
    }

    // Logic kiểm tra dữ liệu nhà cung cấp
    private void validateSupplier(Suppliers supplier) {
        if (supplier.getName() == null || supplier.getName().isEmpty()) {
            throw new IllegalArgumentException("Tên nhà cung cấp không được để trống.");
        }
        if (supplier.getEmail() == null || !supplier.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Email nhà cung cấp không hợp lệ.");
        }
        if (supplier.getPhone() == null || !supplier.getPhone().matches("\\d{10,15}")) {
            throw new IllegalArgumentException("Số điện thoại nhà cung cấp không hợp lệ.");
        }
        if (supplier.getAddress() == null || supplier.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ nhà cung cấp không được để trống.");
        }
    }
}
