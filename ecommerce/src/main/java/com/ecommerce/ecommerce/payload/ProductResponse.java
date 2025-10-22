package com.ecommerce.ecommerce.payload;

import com.ecommerce.ecommerce.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    List<ProductDTO> content;

}
