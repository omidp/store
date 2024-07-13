package com.grocery.store.model;

public record OrderResponse(long id, String description, java.util.List<OrderItemResponse> items) {
}
