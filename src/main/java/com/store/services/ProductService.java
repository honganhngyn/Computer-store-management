package com.store.services;

import com.store.models.Product;
import com.store.repositories.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Lấy danh sách sản phẩm
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.getAllProducts();
        if (products == null) {
            return List.of();
        }
        return products;
    }

    // Thêm mới sản phẩm
    public void addProduct(Product product) {
        validateProduct(product);
        productRepository.addProduct(product);
    }

    // Cập nhật sản phẩm
    public void updateProduct(Product product) {
        validateProduct(product);
        Product existingProduct = productRepository.getProductById(product.getProductId());
        if (existingProduct == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + product.getProductId());
        }
        productRepository.updateProduct(product);
    }

    // Tìm kiếm sản phẩm theo từ khóa
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }

    // Tìm sản phẩm theo ID
    public Product getProductById(int productId) {
        Product product = productRepository.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + productId);
        }
        return product;
    }

    // Logic kiểm tra dữ liệu sản phẩm
    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống.");
        }
        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn 0.");
        }

        if (product.getCategoryId() <= 0) {
            throw new IllegalArgumentException("Danh mục sản phẩm không hợp lệ.");
        }
    }
}
