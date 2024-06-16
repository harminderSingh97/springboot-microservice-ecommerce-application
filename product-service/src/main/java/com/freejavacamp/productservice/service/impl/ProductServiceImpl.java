package com.freejavacamp.productservice.service.impl;

import com.freejavacamp.productservice.dto.ProductRequest;
import com.freejavacamp.productservice.dto.ProductResponse;
import com.freejavacamp.productservice.model.Product;
import com.freejavacamp.productservice.repository.ProductRepository;
import com.freejavacamp.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public void createProduct(ProductRequest productRequest) {
        log.info("=========createProduct() STARTED: =========");
        long startTime = System.currentTimeMillis();
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice()).build();
        productRepository.save(product);
        long endTime = System.currentTimeMillis();
        log.info("=========Product Saved in Databse: {}", product);
        log.info("=========TOTAL TIME EXECUTION: {}", (endTime-startTime)/1000);
        log.info("=========createProduct() ENDED: =========");
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("=========getAllProducts() STARTED: =========");
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
List<Product> products =  productRepository.findAll();

        log.info("=========TOTAL TIME EXECUTION: {}", (endTime-startTime)/1000);
        log.info("=========getAllProducts() ENDED: =========");
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return  ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
