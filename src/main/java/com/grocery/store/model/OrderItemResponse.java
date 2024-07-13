package com.grocery.store.model;

import com.grocery.store.domain.OrderItemEntity;
import com.grocery.store.domain.ProductAttribute;
import com.grocery.store.domain.ProductEntity;
import lombok.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@Value
public class OrderItemResponse {
	private BigDecimal amount;
	private BigDecimal price;
	private int quantity;
	private int orderedQuantity;
	private String message;
	private ProductResponse product;
	private List<OrderAttributeResponse> attributes;

	public static OrderItemResponse of(OrderItemEntity orderItem, ProductEntity product, Map<ProductAttribute, String> attributes) {
		return new OrderItemResponse(
			orderItem.getUnitPrice(),
			orderItem.getPrice(),
			orderItem.getQuantity(),
			orderItem.getOrderedQuantity(),
			orderItem.getMessage(),
			ProductResponse.of(product),
			getAttributes(attributes)
		);
	}

	private static List<OrderAttributeResponse> getAttributes(Map<ProductAttribute, String> attributes) {
		if (attributes == null) {
			return emptyList();
		}
		List<OrderAttributeResponse> resultList = new ArrayList<>();
		attributes.forEach((k, v) -> resultList.add(new OrderAttributeResponse(k, v)));
		return resultList;
	}

}
