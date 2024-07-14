package com.grocery.store.model;

import java.util.List;

public record OrderRequest(List<OrderItemRequest> items) {
}
