package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


    //private List<Category> categories = new ArrayList<>();

    @Override
    public List<Category> getAllCategories() {
      return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(long categoryId) {
        List<Category> categories = categoryRepository.findAll();
        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category is not found"));
        if(category == null)
            return "Category not found";
        categoryRepository.delete(category);
        return "Catgory with catergory ID" + categoryId + " is deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, long categoryId) {
        List<Category> categories = categoryRepository.findAll();
         Optional<Category> optionalCategory = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst();
       if(optionalCategory.isPresent()){
           Category existingCategory = optionalCategory.get();
           existingCategory.setCategoryName(category.getCategoryName());
           Category savedCategory = categoryRepository.save(existingCategory);
           return savedCategory;
       }else{
          throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category is not found");
       }
    }
}
