package com.freejavacamp.inventoryservice.service;

import com.freejavacamp.inventoryservice.dto.InventoryResponse;
import com.freejavacamp.inventoryservice.model.Inventory;

import java.util.List;

public interface InventoryService {
    public List<InventoryResponse> isInStock(List<String> skuCodes);
}
