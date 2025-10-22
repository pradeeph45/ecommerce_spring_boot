package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.payload.ProductDTO;
import com.ecommerce.ecommerce.payload.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductDTO addProduct(ProductDTO product, Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse searchByCategory(Long categoryId);

    ProductResponse SearchProductByKeyword(String keyword);

    ProductDTO updateProduct(ProductDTO product, Long productId);

    ProductDTO deleteProduct(Long productId);
}
