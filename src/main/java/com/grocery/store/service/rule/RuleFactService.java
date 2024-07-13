package com.grocery.store.service.rule;

import com.grocery.store.domain.OrderItemEntity;
import com.grocery.store.domain.ProductAttribute;
import com.grocery.store.domain.ProductEntity;
import com.grocery.store.model.rule.RuleOutputResult;

import java.util.Map;

public interface RuleFactService {

	RuleOutputResult collectFact(ProductEntity product, OrderItemEntity orderItem, Map<ProductAttribute, String> attributes);

}
