package com.store.controllers;

import com.store.models.Product;
import com.store.services.ProductService;
import com.store.utils.FormatUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<Product> products = productService.getAllProducts();

        if (products == null || products.isEmpty()) {
            model.addAttribute("message", "Không có sản phẩm nào!");
            products = new ArrayList<>(); // Khởi tạo danh sách trống để tránh lỗi.
        }

        // Định dạng dữ liệu
        products.forEach(product -> {
            product.setFormattedPrice(FormatUtils.formatCurrency(product.getPrice()));
            product.setFormattedDate(FormatUtils.formatDateTime(product.getCreatedAt()));
        });

        model.addAttribute("products", products);
        model.addAttribute("newProduct", new Product()); // Gửi đối tượng rỗng cho form
        return "products";
    }

    @PostMapping("/products/add")
    @ResponseBody
    public Map<String, String> addProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("categoryId") int categoryId,
            @RequestParam("supplierId") int supplierId) {
        Map<String, String> response = new HashMap<>();
        try {
            Product newProduct = new Product();
            newProduct.setName(name);
            newProduct.setDescription(description);
            newProduct.setPrice(price);
            newProduct.setCategoryId(categoryId);
            newProduct.setSupplierId(supplierId);
            newProduct.setCreatedAt(LocalDateTime.now());

            productService.addProduct(newProduct);
            response.put("message", "Sản phẩm đã được thêm thành công!");
        } catch (Exception e) {
            response.put("error", "Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
        return response;
    }

    // Tìm kiếm sản phẩm theo từ khoá
    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);

        // Định dạng lại ngày tháng cho từng sản phẩm
        products.forEach(product -> {
            product.setFormattedPrice(FormatUtils.formatCurrency(product.getPrice()));
            product.setFormattedDate(FormatUtils.formatDateTime(product.getCreatedAt()));
        });

        return ResponseEntity.ok(products);
    }

    @PostMapping("/products/update")
    @ResponseBody
    public Map<String, String> updateProduct(
            @RequestParam("id") int productId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("categoryId") int categoryId,
            @RequestParam("supplierId") int supplierId,
            @RequestParam("createdAt") String createdAtStr) {
        Map<String, String> response = new HashMap<>();
        try {
            LocalDateTime createdAt = LocalDateTime.parse(createdAtStr);
            Product updatedProduct = new Product(productId, name, description, price, categoryId, supplierId,
                    createdAt);

            productService.updateProduct(updatedProduct);
            response.put("message", "Sản phẩm đã được cập nhật thành công!");
        } catch (Exception e) {
            response.put("error", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
        }
        return response;
    }

    // Tìm sản phẩm theo ID
    public void findProductByID(int productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            System.out.println("Đã tìm thấy sản phẩm!");
        } else {
            System.out.println("Không tìm thấy sản phẩm với ID: " + productId);
        }
    }
}
