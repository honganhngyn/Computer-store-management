package com.store.controllers;

import com.store.models.Employees;
import com.store.services.EmployeeService;
import com.store.utils.FormatUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getEmployees(Model model) {
        List<Employees> employees = employeeService.getAllEmployees();

        if (employees == null || employees.isEmpty()) {
            model.addAttribute("message", "Không có nhân viên nào");
            employees = new ArrayList<>();
        }

        // Định dạng dữ liệu
        employees.forEach(employee -> {
            employee.setFormattedSalary(FormatUtils.formatCurrency(employee.getSalary()));
            employee.setFormattedDate(FormatUtils.formatDateTime(employee.getHiredDate()));
        });

        model.addAttribute("employees", employees);
        model.addAttribute("newEmployee", new Employees());
        return "employee";
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, String> addEmployee(
            @RequestParam("name") String name,
            @RequestParam("username") String username,
            @RequestParam("isAdmin") boolean isAdmin,
            @RequestParam("position") String position,
            @RequestParam("salary") double salary,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone) {
        Map<String, String> response = new HashMap<>();
        try {
            Employees newEmployee = new Employees();
            newEmployee.setName(name);
            newEmployee.setUsername(username);
            newEmployee.setAdmin(isAdmin);
            newEmployee.setPosition(position);
            newEmployee.setSalary(salary);
            newEmployee.setEmail(email);
            newEmployee.setPhone(phone);
            newEmployee.setHiredDate(LocalDateTime.now());

            employeeService.addEmployee(newEmployee);
            response.put("message", "Nhân viên đã được thêm thành công!");
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<Employees>> searchEmployees(@RequestParam String keyword) {
        List<Employees> employees = employeeService.searchEmployees(keyword);

        // Định dạng dữ liệu
        employees.forEach(employee -> {
            employee.setFormattedSalary(FormatUtils.formatCurrency(employee.getSalary()));
            employee.setFormattedDate(FormatUtils.formatDateTime(employee.getHiredDate()));
        });

        return ResponseEntity.ok(employees);
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String, String> updateEmployee(
            @RequestParam("id") int employeeId,
            @RequestParam("name") String name,
            @RequestParam("username") String username,
            @RequestParam("isAdmin") boolean isAdmin,
            @RequestParam("position") String position,
            @RequestParam("salary") double salary,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("hiredDate") String hiredDateStr) {
        Map<String, String> response = new HashMap<>();
        try {
            LocalDateTime hiredDate = LocalDateTime.parse(hiredDateStr);
            Employees updatedEmployee = new Employees(employeeId, name, username, isAdmin, position, salary,
                    email, phone, hiredDate);

            employeeService.updateEmployee(updatedEmployee);
            response.put("message", "Thông tin nhân viên đã được cập nhật thành công!");
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }
}