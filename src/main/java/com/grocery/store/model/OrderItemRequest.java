package com.grocery.store.model;

import com.grocery.store.domain.ProductAttribute;
import jakarta.validation.constraints.Min;

import java.util.Map;

public record OrderItemRequest(Long productId, @Min(value = 1) int qty, Map<ProductAttribute, String> attributes) {
}
