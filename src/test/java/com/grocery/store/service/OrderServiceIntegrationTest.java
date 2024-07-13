package com.grocery.store.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery.store.IntegrationTestContextConfig;
import com.grocery.store.exception.NotFoundException;
import com.grocery.store.model.OrderResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = IntegrationTestContextConfig.class)
@TestPropertySource(locations = "classpath*:/application.properties")
public class OrderServiceIntegrationTest {

	private final ObjectMapper MAPPER = new ObjectMapper();

	@Autowired
	private OrderService orderService;

	@Autowired
	private IntegrationTestContextConfig.OrderId orderId;

	@Test
	void testGetByIdThrowsNotFoundException() throws Exception {
		assertThrows(NotFoundException.class, () -> orderService.getById(nextLong()));
	}

	@Test
	void testGetDetailsById(){
		OrderResponse detailsById = orderService.getDetailsById(orderId.getIdentifier());
		assertNotNull(detailsById);
	}

}
