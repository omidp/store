package com.grocery.store.model.rule;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemRuleOutput {

	private String message;
	private int qty;
	private BigDecimal price;


	public void setActualPrice(int weight, int discountPercentage, BigDecimal price) {
		BigDecimal pricePerWeight = BigDecimal.valueOf(weight / 100);
		BigDecimal discount = pricePerWeight.multiply(price.multiply(BigDecimal.valueOf(discountPercentage).divide(BigDecimal.valueOf(100))));
		this.price = pricePerWeight.multiply(price).subtract(discount);
	}

}
