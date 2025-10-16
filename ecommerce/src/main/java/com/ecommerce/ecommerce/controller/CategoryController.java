package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.model.Category;
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
    public ResponseEntity<List<Category>> getAllCategories(){
     List<Category> categories = categoryService.getAllCategories();
     return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @PostMapping("/api/public/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category){
        categoryService.createCategory(category);
        return new ResponseEntity<>("Categories added successfully",HttpStatus.CREATED);
    }

    @DeleteMapping("api/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable long categoryId) {
        try {
            return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

     @PutMapping("api/public/categories/{categoryId}")
     public ResponseEntity<String> updateCategory(@RequestBody Category category,
                                                 @PathVariable long categoryId){
           try{
               Category savedCategory = categoryService.updateCategory(category,categoryId);
               return new ResponseEntity<>("Category updated successfully",HttpStatus.OK);
           } catch (ResponseStatusException e){
               return new ResponseEntity<>(e.getReason(),e.getStatusCode());
           }
        }

}
