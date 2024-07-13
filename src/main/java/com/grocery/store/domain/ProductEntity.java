package com.grocery.store.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "product")
@AttributeOverrides({
	@AttributeOverride(name = "id", column = @Column(name = "product_id"))
})
@Data
@NoArgsConstructor
public class ProductEntity extends PO {

	@Enumerated(EnumType.STRING)
	private Category category;

	private LocalDate expiryDate;

	/**
	 * Manufacture date
	 */
	private LocalDate mfgDate;

	private String name;

	private BigDecimal price;

	public ProductEntity(long id, Category category, LocalDate expiryDate, String name, BigDecimal price, LocalDate manufactureDate) {
		setId(id);
		this.category = category;
		this.expiryDate = expiryDate;
		this.name = name;
		this.price = price;
		this.mfgDate = manufactureDate;
	}

	@Transient
	public long getExpiryDays() {
		if (getMfgDate() == null) {
			return 0;
		}
		return ChronoUnit.DAYS.between(getMfgDate(), LocalDate.now());
	}

}
