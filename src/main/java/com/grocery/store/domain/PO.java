package com.grocery.store.domain;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

/**
 * Persistence Object
 */
@MappedSuperclass
@Data
public abstract class PO {

	@Id
	private long id;
}
