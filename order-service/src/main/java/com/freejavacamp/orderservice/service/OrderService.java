package com.freejavacamp.orderservice.service;

import com.freejavacamp.orderservice.dto.OrderRequest;

public interface OrderService {
    public  void placeOrder(OrderRequest orderRequest);
}
