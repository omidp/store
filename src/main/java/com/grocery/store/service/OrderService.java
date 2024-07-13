package com.grocery.store.service;

import com.grocery.store.domain.Category;
import com.grocery.store.domain.OrderEntity;
import com.grocery.store.domain.OrderItemEntity;
import com.grocery.store.domain.ProductAttribute;
import com.grocery.store.domain.ProductEntity;
import com.grocery.store.exception.NotFoundException;
import com.grocery.store.model.OrderItemResponse;
import com.grocery.store.model.rule.OrderItemRuleOutput;
import com.grocery.store.model.OrderItemTotalResponse;
import com.grocery.store.model.OrderResponse;
import com.grocery.store.model.OrderTotalResponse;
import com.grocery.store.service.rule.RuleFactService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.RandomUtils.nextLong;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final SessionFactory sessionFactory;
	private final RuleFactService ruleFactService;

	public OrderTotalResponse getById(Long orderId) {
		String hql = """
			select NEW com.grocery.store.model.OrderItemTotalResponse(cast(sum(oi.price) as java.math.BigDecimal), cast(sum(oi.quantity) as Integer), p.category) 
			from OrderItemEntity oi
			left join oi.product p					
			where oi.order.id = :orderId
			group by p.category
			""";
		return sessionFactory.fromSession(session -> {
			OrderEntity orderEntity = session.get(OrderEntity.class, orderId);
			if (orderEntity == null) {
				throw new NotFoundException("Order not found. orderId #" + orderId);
			}
			List<OrderItemTotalResponse> resultList = session.createSelectionQuery(hql, OrderItemTotalResponse.class).setParameter("orderId", orderId).getResultList();
			BigDecimal totalAmount = resultList.stream().map(OrderItemTotalResponse::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
			return new OrderTotalResponse(totalAmount, resultList);
		});
	}

	public OrderResponse getDetailsById(Long orderId) {
		String hql = """
				from OrderItemEntity oi 
				left join fetch oi.product p
				left join fetch oi.attributes attr
				where oi.order.id = :orderId
				""";
		return sessionFactory.fromSession(session -> {
			//TODO: fetch data with one query
			OrderEntity orderEntity = session.get(OrderEntity.class, orderId);
			if (orderEntity == null) {
				throw new NotFoundException("Order not found. orderId #" + orderId);
			}
			List<OrderItemResponse> items = session.createSelectionQuery(hql, OrderItemEntity.class).setParameter("orderId", orderId).getResultList()
				.stream().map(orderItem -> OrderItemResponse.of(orderItem, orderItem.getProduct(), orderItem.getAttributes()))
				.toList();
			return new OrderResponse(orderId, orderEntity.getDescription(), items);
		});
	}


	public void insertOrderItem(ProductEntity product, OrderEntity order, Map<ProductAttribute, String> attributes, int qty, Session session) {
		if (Category.BREADS.equals(product.getCategory()) && product.getExpiryDays() > 6) {
			return;
		}
		OrderItemEntity orderItem = new OrderItemEntity(nextLong(), product.getPrice(), qty, product, order);
		if (attributes != null) {
			orderItem.setAttributes(attributes);
		}
		OrderItemRuleOutput output = ruleFactService.collectFact(product, orderItem, orderItem.getAttributes()).getResult();
		if (output != null) {
			orderItem.setPrice(output.getPrice());
			orderItem.setQuantity(output.getQty());
			orderItem.setMessage(output.getMessage());
		}
		session.persist(orderItem);
	}

}
