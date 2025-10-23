package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.config.AppConstants;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.payload.ProductDTO;
import com.ecommerce.ecommerce.payload.ProductResponse;
import com.ecommerce.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO product, @PathVariable Long categoryId){

        ProductDTO productDTO = productService.addProduct(product,categoryId);
        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER) String sortOrder
    ){
        ProductResponse productDTOS = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productDTOS,HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategories(@PathVariable Long categoryId){
        ProductResponse productResponse = productService.searchByCategory(categoryId);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("public/products/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword){
        ProductResponse productResponse = productService.SearchProductByKeyword(keyword);
        return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
    }

    @PutMapping("admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO product,@PathVariable Long productId){
        ProductDTO updatedProductDTO = productService.updateProduct(product,productId);
        return new ResponseEntity<>(updatedProductDTO,HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO deletedProductDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProductDTO,HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image")MultipartFile image) throws IOException {
       ProductDTO updatedProduct = productService.updateProductImage(productId,image);
       return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }

}
