package com.grocery.store.model;

import com.grocery.store.domain.Category;
import com.grocery.store.domain.ProductEntity;

import java.math.BigDecimal;
import java.time.LocalDate;


public record ProductResponse(long id, String name, BigDecimal price, Category category, LocalDate expiryDate, LocalDate manufactureDate) {
	public static ProductResponse of(ProductEntity productEntity) {
		return new ProductResponse(
			productEntity.getId(),
			productEntity.getName(),
			productEntity.getPrice(),
			productEntity.getCategory(),
			productEntity.getExpiryDate(),
			productEntity.getMfgDate()
		);
	}

}