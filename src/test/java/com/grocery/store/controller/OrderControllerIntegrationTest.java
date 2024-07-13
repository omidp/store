package com.grocery.store.controller;

import com.grocery.store.IntegrationTestContextConfig;
import com.grocery.store.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = IntegrationTestContextConfig.class)
@TestPropertySource(locations = "classpath*:/application.properties")
public class OrderControllerIntegrationTest {

	@Autowired
	private OrderService orderService;

	@Test
	void testGetDetails() {
//		orderService.getById(123L);
	}

}
