package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.model.Category;
import com.ecommerce.ecommerce.model.Product;
import com.ecommerce.ecommerce.payload.ProductDTO;
import com.ecommerce.ecommerce.payload.ProductResponse;
import com.ecommerce.ecommerce.repository.CategoryRepository;
import com.ecommerce.ecommerce.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow( () ->
                new ResourceAccessException("Category Id not found Exception"));
        Product product = modelMapper.map(productDTO,Product.class);
        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortBYAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortBYAndOrder);
        Page<Product> pageProducts = productRepository.findAll(pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow( () ->
                new ResourceAccessException("Category with that categoryId is not found")
        );
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse SearchProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%");
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceAccessException("Product with product is not found"));
        Product product = modelMapper.map(productDTO,Product.class);
        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setSpecialPrice(product.getSpecialPrice());
        Product savedProduct = productRepository.save(existingProduct);

        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceAccessException("Product with product ID is not found"));
        productRepository.delete(product);
        return modelMapper.map(product,ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // Get the product from DB
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceAccessException("Product with product ID is not found"));
        //Upload image to server
        //Get the file name of the uploaded image

        String fileName = fileService.uploadImage(path,image);

        existingProduct.setImage(fileName);

        //Updating the new file name to the product
        Product updatedProduct = productRepository.save(existingProduct);

        //return DTO after mapping product to DTO

        return modelMapper.map(updatedProduct,ProductDTO.class);
    }
}
