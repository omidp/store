package com.grocery.store.model;

import com.grocery.store.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemTotalResponse {
	private BigDecimal amount;
	private int quantity;
	private Category category;

}

