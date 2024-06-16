package com.freejavacamp.inventoryservice.service.impl;

import com.freejavacamp.inventoryservice.dto.InventoryResponse;
import com.freejavacamp.inventoryservice.repository.InventoryRepository;
import com.freejavacamp.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        final List<String> skuCodeNotExist = new ArrayList<>();
        List<InventoryResponse> inventoryResponses = inventoryRepository.findBySkuCodeIn(skuCodes).stream().map(inventory -> {
                    InventoryResponse inventoryResponse = InventoryResponse.builder().isInStock(inventory.getQuantity() > 0)
                            .skuCode(inventory.getSkuCode())
                            .build();
                    if (inventory.getQuantity() == 0) {
                        skuCodeNotExist.add(inventory.getSkuCode());
                    }
                    return inventoryResponse;
                }
        ).toList();

        if (skuCodes.size() != inventoryResponses.size()) {
            if (!skuCodeNotExist.isEmpty()) {
                throw new IllegalArgumentException("Item with skuCode: " + skuCodeNotExist + " Not Exist in inventory.");
            } else {
                throw new IllegalArgumentException("Inventory Not Exist.");
            }
        }
        log.info("====INVENTORY LIST: {}", inventoryResponses);

        return inventoryResponses;
    }
}
