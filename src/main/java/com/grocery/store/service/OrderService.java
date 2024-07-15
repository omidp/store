package com.grocery.store.service;

import com.grocery.store.domain.Category;
import com.grocery.store.domain.OrderEntity;
import com.grocery.store.domain.OrderItemEntity;
import com.grocery.store.domain.ProductAttribute;
import com.grocery.store.domain.ProductEntity;
import com.grocery.store.exception.NotFoundException;
import com.grocery.store.model.OrderItemRequest;
import com.grocery.store.model.OrderItemResponse;
import com.grocery.store.model.OrderItemTotalResponse;
import com.grocery.store.model.OrderRequest;
import com.grocery.store.model.OrderResponse;
import com.grocery.store.model.OrderTotalResponse;
import com.grocery.store.model.rule.OrderItemRuleOutput;
import com.grocery.store.service.rule.RuleFactService;
import com.grocery.store.util.ManagedSessionFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.RandomUtils.nextLong;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final RuleFactService ruleFactService;
	private final ManagedSessionFactory managedSessionFactory;

	public OrderTotalResponse getById(Long orderId) {
		String hql = """
			select NEW com.grocery.store.model.OrderItemTotalResponse(cast(sum(oi.price) as java.math.BigDecimal), cast(sum(oi.quantity) as Integer), p.category) 
			from OrderItemEntity oi
			left join oi.product p					
			where oi.order.id = :orderId
			group by p.category
			""";
		return managedSessionFactory.fromSession(session -> {
			OrderEntity orderEntity = session.get(OrderEntity.class, orderId);
			if (orderEntity == null) {
				throw new NotFoundException("Order not found. orderId #" + orderId);
			}
			List<OrderItemTotalResponse> resultList = session.createSelectionQuery(hql, OrderItemTotalResponse.class)
				.setParameter("orderId", orderId)
				.getResultList();
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
		return managedSessionFactory.fromSession(session -> {
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

	public long insertOrderItem(OrderRequest orderRequest) {
		return managedSessionFactory.fromTransaction(session -> {
			long orderId = nextLong();
			OrderEntity order = new OrderEntity(orderId, "Order #" + orderId);
			session.persist(order);
			for (OrderItemRequest item : orderRequest.items()) {
				insertOrderItem(session.getReference(ProductEntity.class, item.productId()), orderId, item.attributes(), item.qty());
			}
			return orderId;
		});
	}

	public void insertOrderItem(ProductEntity product, Long orderId, Map<ProductAttribute, String> attributes, int qty) {
		if (Category.BREADS.equals(product.getCategory()) && product.getExpiryDays() > 6) {
			return;
		}
		if (attributes == null) {
			attributes = emptyMap();
		}
		Session currentSession = managedSessionFactory.getCurrentSession();
		OrderItemEntity orderItem = new OrderItemEntity(nextLong(), product.getPrice(), qty, product, currentSession.getReference(OrderEntity.class, orderId));
		orderItem.setAttributes(attributes);
		OrderItemRuleOutput output = ruleFactService.collectFact(product, orderItem, orderItem.getAttributes()).getResult();
		if (output != null) {
			orderItem.setPrice(output.getPrice());
			orderItem.setQuantity(output.getQty());
			orderItem.setMessage(output.getMessage());
		} else {
			orderItem.setPrice(orderItem.getUnitPrice());
			orderItem.setQuantity(orderItem.getOrderedQuantity());
		}
		currentSession.persist(orderItem);
	}

}
