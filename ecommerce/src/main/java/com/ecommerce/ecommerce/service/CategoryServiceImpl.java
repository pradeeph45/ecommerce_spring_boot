package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.payload.CategoryDTO;
import com.ecommerce.ecommerce.payload.CategoryResponse;
import com.ecommerce.ecommerce.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

   @Autowired
   private ModelMapper modelMapper;


    //private List<Category> categories = new ArrayList<>();

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> page = categoryRepository.findAll(pageDetails);

      List<Category> categories = page.getContent();

      List<CategoryDTO> categoryDTOS = categories.stream()
              .map(category -> modelMapper.map(category, CategoryDTO.class))
              .toList();
      CategoryResponse categoryResponse = new CategoryResponse();
      categoryResponse.setCategories(categoryDTOS);
      return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO category) {
       // category.setCategoryId(null);
        Category category1 = modelMapper.map(category,Category.class);
        Category savedCategory =  categoryRepository.save(category1);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category not found"));
        CategoryDTO categoryDTO = modelMapper.map(existingCategory,CategoryDTO.class);
        categoryRepository.delete(existingCategory);
        return categoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO category, Long categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category not found"));
        Category category1 = modelMapper.map(category,Category.class);
        category1.setCategoryId(categoryId);
        existingCategory = categoryRepository.save(category1);
        return modelMapper.map(existingCategory,CategoryDTO.class);

    }
}
