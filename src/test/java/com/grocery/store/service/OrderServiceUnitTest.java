package com.grocery.store.service;

import com.grocery.store.domain.ProductEntity;
import com.grocery.store.exception.NotFoundException;
import com.grocery.store.model.OrderTotalResponse;
import com.grocery.store.service.rule.RuleFactService;
import com.grocery.store.util.ManagedSessionFactory;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static com.grocery.store.domain.Category.BREADS;
import static java.math.BigDecimal.ONE;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUnitTest {

	@Mock
	private ManagedSessionFactory mockSessionFactory;
	@Mock
	private RuleFactService mockRuleFactService;

	private OrderService orderService;

	@BeforeEach
	void setUp() {
		this.orderService = new OrderService(mockRuleFactService, mockSessionFactory);
	}

	@Test
	void testGetById() {
		var result = new OrderTotalResponse(BigDecimal.ONE, Collections.emptyList());
		when(mockSessionFactory.fromSession(any())).thenReturn(result);
		OrderTotalResponse actual = orderService.getById(nextLong());
		assertThat(actual).isEqualTo(result);
	}

	@Test
	void testGetByIdThrowsNotFoundException() {
		when(mockSessionFactory.fromSession(any())).thenThrow(NotFoundException.class);
		assertThrows(NotFoundException.class, () -> orderService.getById(nextLong()));
	}

	@Test
	void testDontAddBreadsOlderThan6Days(){
		var p = new ProductEntity(
			25,
			BREADS,
			LocalDate.now().plusDays(10),
			"Sangak",
			ONE,
			LocalDate.now().minusDays(10)
		);
		orderService.insertOrderItem(p, nextLong(), emptyMap(), 10);
		verifyNoInteractions(mockRuleFactService);
	}

}
