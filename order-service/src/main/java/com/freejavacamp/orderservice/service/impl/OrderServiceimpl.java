package com.freejavacamp.orderservice.service.impl;

import com.freejavacamp.orderservice.dto.InventoryResponse;
import com.freejavacamp.orderservice.dto.OrderLineItemDto;
import com.freejavacamp.orderservice.dto.OrderRequest;
import com.freejavacamp.orderservice.model.Order;
import com.freejavacamp.orderservice.model.OrderLineItem;
import com.freejavacamp.orderservice.repository.OrderRepository;
import com.freejavacamp.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceimpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        log.info("=====placeOrder() STARTED: ==========");
        long startTime = System.currentTimeMillis();
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemList(orderRequest
                        .getOrderLineItemDtoList()
                        .stream()
                        .map(this::mapToDto)
                        .toList())
                .build();
        /* Making Microservice call to Inventory Service Layer */
        List<String> skuCodes = order.getOrderLineItemList().stream().map(OrderLineItem::getSkuCode).toList();
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build()
                ).retrieve().bodyToMono(InventoryResponse[].class).block();
        boolean isOrderPlacedWithAvailableInventory = false;
        if (inventoryResponseArray != null) {
            isOrderPlacedWithAvailableInventory = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::getIsInStock);
        }
        if (isOrderPlacedWithAvailableInventory) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in Stock , Please try later.");
        }

        long endTime = System.currentTimeMillis();
        log.info("====TIME EXECUTION: {}", (endTime - startTime) / 1000);
        log.info("=====placeOrder() ENDED: ==========");

    }

    private OrderLineItem mapToDto(OrderLineItemDto order) {
        return OrderLineItem.builder()
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .skuCode(order.getSkuCode()).
                build();
    }
}
