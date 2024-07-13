package com.grocery.store.model;

import java.math.BigDecimal;

public record OrderTotalResponse(BigDecimal totalAmount, java.util.List<OrderItemTotalResponse> items) {
}
