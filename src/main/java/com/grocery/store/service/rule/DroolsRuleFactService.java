package com.grocery.store.service.rule;

import com.grocery.store.domain.OrderItemEntity;
import com.grocery.store.domain.ProductAttribute;
import com.grocery.store.domain.ProductEntity;
import com.grocery.store.model.rule.OrderItemRuleInput;
import com.grocery.store.model.rule.RuleOutputResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DroolsRuleFactService implements RuleFactService {

	private final RuleService ruleService;

	@Override
	public RuleOutputResult collectFact(ProductEntity product, OrderItemEntity orderItem) {
		OrderItemRuleInput ruleInput = new OrderItemRuleInput(product.getCategory().name(), orderItem.getOrderedQuantity(), orderItem.getUnitPrice(), product.getName());
		switch (product.getCategory()) {
			case VEGETABLES -> ruleInput.setWeight(Integer.parseInt(orderItem.getAttributes().get(ProductAttribute.WEIGHT)));
			case BREADS -> ruleInput.setNumberOfDaysExpired(product.getExpiryDays());
		}
		return ruleService.execute(ruleInput);
	}
}
