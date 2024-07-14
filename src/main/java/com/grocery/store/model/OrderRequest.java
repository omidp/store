package com.grocery.store.model;

import jakarta.validation.Valid;

import java.util.List;

public record OrderRequest(@Valid List<OrderItemRequest> items) {
}
