package com.grocery.store.model;

import com.grocery.store.domain.ProductAttribute;

public record OrderAttributeResponse(ProductAttribute attribute, String value) {
}
