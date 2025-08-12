package com.store.services;

import com.store.models.Employees;
import com.store.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Lấy danh sách nhân viên
    public List<Employees> getAllEmployees() {
        List<Employees> employees = employeeRepository.getAllEmployees();
        return employees == null ? List.of() : employees;
    }

    // Thêm nhân viên mới
    public void addEmployee(Employees employee) {
        validateEmployee(employee);
        employeeRepository.addEmployee(employee);
    }

    // Cập nhật thông tin nhân viên
    public void updateEmployee(Employees employee) {
        validateEmployee(employee);
        Employees existingEmployee = employeeRepository.getEmployeeById(employee.getEmployeeId());
        if (existingEmployee == null) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên với ID: " + employee.getEmployeeId());
        }
        employeeRepository.updateEmployee(employee);
    }

    // Tìm kiếm nhân viên theo từ khóa
    public List<Employees> searchEmployees(String keyword) {
        return employeeRepository.searchEmployees(keyword);
    }

    // Tìm nhân viên theo ID
    public Employees getEmployeeById(int employeeId) {
        Employees employee = employeeRepository.getEmployeeById(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên với ID: " + employeeId);
        }
        return employee;
    }

    // Logic kiểm tra dữ liệu nhân viên
    private void validateEmployee(Employees employee) {
        if (employee.getName() == null || employee.getName().isEmpty()) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống.");
        }
        if (employee.getUsername() == null || employee.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");
        }
        if (employee.getEmail() == null || !employee.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Email nhân viên không hợp lệ.");
        }
        if (employee.getPhone() == null || !employee.getPhone().matches("\\d{10,15}")) {
            throw new IllegalArgumentException("Số điện thoại nhân viên không hợp lệ.");
        }
        if (employee.getSalary() <= 0) {
            throw new IllegalArgumentException("Lương phải lớn hơn 0.");
        }
        if (employee.getPosition() == null ||
                (!employee.getPosition().equalsIgnoreCase("Admin") &&
                        !employee.getPosition().equalsIgnoreCase("Staff"))) {
            throw new IllegalArgumentException("Chức vụ chỉ được phép là 'Admin' hoặc 'Staff'.");
        }
    }
}