package com.freejavacamp.orderservice.dto;

import com.freejavacamp.orderservice.model.OrderLineItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemDto {
    private  Long id;
    private String skuCode;
    private BigDecimal price;
    private  Integer quantity;
}
