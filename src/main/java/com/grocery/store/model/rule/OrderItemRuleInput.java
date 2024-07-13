package com.grocery.store.model.rule;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemRuleInput {

	private String category;
	private Integer qty;
	private BigDecimal price;
	private String name;
	private Long numberOfDaysExpired;
	private Integer weight;

	public OrderItemRuleInput(String category, Integer qty, BigDecimal price, String name) {
		this.category = category;
		this.qty = qty;
		this.price = price;
		this.name = name;
	}
}
