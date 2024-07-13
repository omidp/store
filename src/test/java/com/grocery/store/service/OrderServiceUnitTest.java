package com.grocery.store.service;

import com.grocery.store.exception.NotFoundException;
import com.grocery.store.model.OrderTotalResponse;
import com.grocery.store.service.rule.RuleFactService;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUnitTest {

	@Mock
	private SessionFactory mockSessionFactory;
	@Mock
	private RuleFactService mockRuleFactService;

	private OrderService orderService;

	@BeforeEach
	void setUp() {
		this.orderService = new OrderService(mockSessionFactory, mockRuleFactService);
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

}
