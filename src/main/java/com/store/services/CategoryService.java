package com.store.services;

import com.store.models.Categories;
import com.store.repositories.CategoryRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Lấy tất cả danh mục
    public List<Categories> getAllCategories() {
        List<Categories> categories = categoryRepository.getAllCategories();
        if (categories == null) {
            return List.of();
        }
        return categories;
    }

    // Tìm danh mục theo từ khoá
    public List<Categories> searchCategories(String keyword) {
        return categoryRepository.searchCategories(keyword);
    }

    // Thêm danh mục mới
    public void addCategory(Categories category) {
        validateCategory(category);
        categoryRepository.addCategory(category);
    }

    // Logic kiểm tra dữ liệu danh mục
    public void validateCategory(Categories category) {
        if (category.getName() == null || category.getName().isEmpty()) {
            throw new IllegalArgumentException("Tên danh mục không được để trống.");
        }
        if (category.getName().length() > 255) {
            throw new IllegalArgumentException("Tên danh mục không được vượt quá 255 ký tự.");
        }
    }
}
