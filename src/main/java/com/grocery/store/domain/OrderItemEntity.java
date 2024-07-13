package com.grocery.store.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "store_order_line")
@AttributeOverrides({
	@AttributeOverride(name = "id", column = @Column(name = "order_item_id"))
})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItemEntity extends PO {

	private BigDecimal unitPrice;
	private BigDecimal price;
	private int orderedQuantity;
	private int quantity;
	private String message;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private ProductEntity product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private OrderEntity order;

	@ElementCollection
	@CollectionTable(
		name = "order_attributes",
		joinColumns = @JoinColumn(name = "order_item_id")
	)
	@MapKeyEnumerated(EnumType.STRING)
	@Column(name = "attribute_value")
	private Map<ProductAttribute, String> attributes = new HashMap<>();

	public OrderItemEntity(long id, BigDecimal unitPrice, int orderedQuantity, ProductEntity product, OrderEntity order) {
		setId(id);
		this.unitPrice = unitPrice;
		this.orderedQuantity = orderedQuantity;
		this.product = product;
		this.order = order;
	}

}
