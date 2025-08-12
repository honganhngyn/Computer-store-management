package com.store.controllers;

import com.store.models.Categories;
import com.store.services.CategoryService;

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
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public String showCategories(Model model) {
        List<Categories> categories = categoryService.getAllCategories();

        if (categories == null || categories.isEmpty()) {
            model.addAttribute("message", "Không có loại sản phẩm nào!");
            categories = new ArrayList<>();
        }
        model.addAttribute("categories", categories);
        model.addAttribute("newCategory", new Categories());
        return "categories";
    }

    // Tìm kiếm sản phẩm theo từ khoá
    @GetMapping("/categories/search")
    public ResponseEntity<List<Categories>> searchCategories(@RequestParam String keyword) {
        List<Categories> categories = categoryService.searchCategories(keyword);

        return ResponseEntity.ok(categories);
    }

    // Thêm danh mục mới
    @PostMapping("/categories/add")
    @ResponseBody
    public Map<String, String> addCategory(
            @RequestParam("name") String name) {
        Map<String, String> response = new HashMap<>();
        try {
            List<Categories> existingCategories = categoryService.searchCategories(name);
            if (!existingCategories.isEmpty()) {
                response.put("error", "Tên loại sản phẩm đã tồn tại.");
                return response;
            }
            Categories newCategories = new Categories();
            newCategories.setName(name);

            categoryService.addCategory(newCategories);
            response.put("message", "Loại sản phẩm đã được thêm thành công!");
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }
}
