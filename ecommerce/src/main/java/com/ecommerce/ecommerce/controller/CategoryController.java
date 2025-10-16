package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.payload.CategoryDTO;
import com.ecommerce.ecommerce.payload.CategoryResponse;
import com.ecommerce.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class CategoryController {

   private final CategoryService categoryService;

   public CategoryController(CategoryService categoryService){
       this.categoryService = categoryService;
   }

    @GetMapping("/api/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(){
     CategoryResponse categories = categoryService.getAllCategories();
     return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @PostMapping("/api/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO category){
        CategoryDTO categoryDTO = categoryService.createCategory(category);
        return new ResponseEntity<>(categoryDTO,HttpStatus.CREATED);
    }

    @DeleteMapping("api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable long categoryId) {

            return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
      
    }

     @PutMapping("api/public/categories/{categoryId}")
     public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO category,
                                                 @PathVariable Long categoryId){

               CategoryDTO savedCategory = categoryService.updateCategory(category,categoryId);
               return new ResponseEntity<>(savedCategory,HttpStatus.OK);

        }

}
