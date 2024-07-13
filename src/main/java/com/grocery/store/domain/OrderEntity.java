package com.grocery.store.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store_order")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderEntity extends PO {

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	private List<OrderItemEntity> orderItems = new ArrayList<>();
	private String description;

	public OrderEntity(long id, String description) {
		setId(id);
		this.description = description;
	}

}
