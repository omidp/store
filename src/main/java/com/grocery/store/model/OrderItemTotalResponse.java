package com.grocery.store.model;

import com.grocery.store.domain.Category;
import com.grocery.store.domain.OrderItemEntity;
import com.grocery.store.domain.ProductAttribute;
import com.grocery.store.domain.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemTotalResponse {
	private BigDecimal amount;
	private int quantity;
	private Category category;

}

