package com.freejavacamp.productservice.service;

import com.freejavacamp.productservice.dto.ProductRequest;
import com.freejavacamp.productservice.dto.ProductResponse;
import com.freejavacamp.productservice.model.Product;

import java.util.List;

public interface ProductService {
    public  void createProduct(ProductRequest productRequest);

   public List<ProductResponse> getAllProducts();
}
