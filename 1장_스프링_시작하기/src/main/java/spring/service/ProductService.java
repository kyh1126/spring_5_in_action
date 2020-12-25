package spring.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductService {
    private final InventoryService inventoryService;
}
