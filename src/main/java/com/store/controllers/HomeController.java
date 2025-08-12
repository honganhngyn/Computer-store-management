package com.store.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/")
    public String homePage(Model model) {
        return "index";
    }

    @GetMapping("/products")
    public String productsPage() {
        return "products";
    }

    @GetMapping("/categories")
    public String categoriesPage() {
        return "categories";
    }

    @GetMapping("/customers")
    public String customersPage() {
        return "customers";
    }

    @GetMapping("/orders")
    public String ordersPage() {
        return "orders";
    }

    @GetMapping("/inventory")
    public String inventoryPage() {
        return "inventory";
    }

    @GetMapping("/suppliers")
    public String suppliersPage() {
        return "suppliers";
    }

    @GetMapping("/employees")
    public String employeesPage() {
        return "employees";
    }

    @GetMapping("/payments")
    public String paymentsPage() {
        return "payments";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
