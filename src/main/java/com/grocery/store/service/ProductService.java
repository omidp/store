package com.grocery.store.service;

import com.grocery.store.domain.ProductEntity;
import com.grocery.store.model.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final SessionFactory sessionFactory;

	public List<ProductResponse> get() {
		return sessionFactory.fromTransaction(session -> session.createSelectionQuery("from ProductEntity p", ProductEntity.class)
			.getResultList()
			.stream()
			.map(this::convert)
			.toList());
	}

	private ProductResponse convert(ProductEntity product) {
		return ProductResponse.of(product);
	}
}
