package com.grocery.store.model.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleOutputResult {

	private OrderItemRuleOutput result;

	public void setResult(OrderItemRuleOutput output) {
		this.result = output;
	}

	public BigDecimal valueOf(Number input) {
		return new BigDecimal(input.doubleValue());
	}

}
