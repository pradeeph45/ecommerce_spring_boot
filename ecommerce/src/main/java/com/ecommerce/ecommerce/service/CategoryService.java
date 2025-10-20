package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.payload.CategoryDTO;
import com.ecommerce.ecommerce.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    CategoryDTO createCategory(CategoryDTO category);


    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO category,Long categoryId);
}
