package com.grocery.store.service;

import com.grocery.store.domain.OrderEntity;
import com.grocery.store.domain.ProductAttribute;
import com.grocery.store.domain.ProductEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

import static com.grocery.store.domain.Category.BEERS;
import static com.grocery.store.domain.Category.BREADS;
import static com.grocery.store.domain.Category.VEGETABLES;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.valueOf;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.RandomUtils.nextLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicDataSetup {

	private final SessionFactory sessionFactory;
	private final OrderService orderService;

	/**
	 * @return orderId
	 */
	public Long init() {
		sessionFactory.inTransaction(this::initProducts);
		return sessionFactory.fromTransaction(this::initOrders);
	}

	private Long initOrders(Session session) {
		long orderId = nextLong();
		log.info("##################################################################");
		log.info("Creating order with id {}", orderId);
		log.info("##################################################################");
		OrderEntity order = new OrderEntity(orderId, "Order #" + orderId);
		session.persist(order);
		orderBread(session, order);
		orderVegetables(session, order);
		orderBeers(session, order);
		return orderId;
	}

	private void orderBeers(Session session, OrderEntity order) {
		ProductEntity product = session.get(ProductEntity.class, 14L);
		orderService.insertOrderItem(product, order, emptyMap(), 6, session);
	}

	private void orderVegetables(Session session, OrderEntity order) {
		ProductEntity product = session.get(ProductEntity.class, 16L);
		orderService.insertOrderItem(product, order, Map.of(ProductAttribute.WEIGHT, "200"), 1, session);
	}

	private void orderBread(Session session, OrderEntity order) {
		ProductEntity product = session.get(ProductEntity.class, 11L);
		orderService.insertOrderItem(product, order, emptyMap(), 1, session);
		ProductEntity product2 = session.get(ProductEntity.class, 10L);
		orderService.insertOrderItem(product2, order, emptyMap(), 1, session);
	}

	private void initProducts(Session session) {
		initBreads(session);
		initVegetables(session);
		initBeers(session);
	}

	private void initBeers(Session session) {
		session.persist(new ProductEntity(13, BEERS, null, "Belgium beer", valueOf(0.5), null));
		session.persist(new ProductEntity(14, BEERS, null, "Dutch beer", valueOf(0.5), null));
		session.persist(new ProductEntity(15, BEERS, null, "German beer", valueOf(0.5), null));
		session.persist(new ProductEntity(21, BEERS, null, "Aragh Sagi", valueOf(0.5), null));
	}

	private void initVegetables(Session session) {
		session.persist(new ProductEntity(16, VEGETABLES, null, "Lettuce", ONE, null));
		session.persist(new ProductEntity(17, VEGETABLES, null, "Cucumber", ONE, null));
		session.persist(new ProductEntity(18, VEGETABLES, null, "Celery", ONE, null));
	}

	private void initBreads(Session session) {
		session.persist(new ProductEntity(
			10,
			BREADS,
			LocalDate.now().plusDays(7),
			"Suikerbrood",
			ONE,
			LocalDate.now()
		));
		session.persist(new ProductEntity(
			11,
			BREADS,
			LocalDate.now().plusDays(7),
			"Krentenbollen",
			ONE,
			LocalDate.now().minusDays(3)
		));
		session.persist(new ProductEntity(
			12,
			BREADS,
			LocalDate.now().plusDays(6),
			"Kerststol",
			ONE,
			LocalDate.now().minusDays(6)
		));
		session.persist(new ProductEntity(
			25,
			BREADS,
			LocalDate.now().plusDays(10),
			"Sangak",
			ONE,
			LocalDate.now().minusDays(10)
		));
	}

}
