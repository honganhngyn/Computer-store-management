package com.store.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
public class Employees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private int employeeId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "username", nullable = false, unique = true, length = 15)
    private String username;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @Column(name = "position", length = 50)
    private String position; // chức vụ: admin hay nhân viên

    @Column(name = "salary", nullable = false)
    private double salary;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "hired_date", nullable = false, updatable = false)
    private LocalDateTime hiredDate;

    @Transient
    private String formattedSalary;

    @Transient
    private String formattedDate;

    public Employees() {
    }

    public Employees(int employeeId, String name, String username, boolean isAdmin,
            String position, double salary, String email, String phone, LocalDateTime hiredDate) {
        this.employeeId = employeeId;
        this.name = name;
        this.username = username;
        this.isAdmin = isAdmin;
        this.position = position;
        this.salary = salary;
        this.email = email;
        this.phone = phone;
        this.hiredDate = hiredDate;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getHiredDate() {
        return hiredDate;
    }

    public void setHiredDate(LocalDateTime hiredDate) {
        this.hiredDate = hiredDate;
    }

    public String getFormattedSalary() {
        return formattedSalary;
    }

    public void setFormattedSalary(String formattedSalary) {
        this.formattedSalary = formattedSalary;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", username=" + username + '\'' +
                ", isAdmin=" + isAdmin + '\'' +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", hiredDate=" + hiredDate +
                '}';
    }
}
