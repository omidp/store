package com.grocery.store.model;

import com.grocery.store.domain.ProductAttribute;

import java.util.Map;

public record OrderItemRequest(Long productId, int qty, Map<ProductAttribute, String> attributes) {
}
