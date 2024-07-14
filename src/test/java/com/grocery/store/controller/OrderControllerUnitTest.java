package com.grocery.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery.store.model.OrderItemTotalResponse;
import com.grocery.store.model.OrderRequest;
import com.grocery.store.model.OrderTotalResponse;
import com.grocery.store.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static com.grocery.store.domain.Category.BEERS;
import static com.grocery.store.domain.Category.BREADS;
import static com.grocery.store.domain.Category.VEGETABLES;
import static java.math.BigDecimal.valueOf;
import static org.apache.commons.lang3.RandomUtils.nextLong;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class OrderControllerUnitTest {
	private MockMvc mockMvc;
	@Mock private OrderService mockOrderService;
	private final ObjectMapper MAPPER = new ObjectMapper();
	private OrderController orderController;

	@BeforeEach
	void setup() {
		orderController = new OrderController(mockOrderService);
		mockMvc = MockMvcBuilders.standaloneSetup(orderController)
			.setControllerAdvice(new RestExceptionController())
			.build();
	}

	@Test
	void testGetById() throws Exception {
		var items = List.of(
			new OrderItemTotalResponse(new BigDecimal("1.00"), 6, BEERS),
			new OrderItemTotalResponse(new BigDecimal("2.00"), 3, BREADS),
			new OrderItemTotalResponse(valueOf(1.86), 1, VEGETABLES)
		);
		var expected = new OrderTotalResponse(valueOf(4.86), items);
		when(mockOrderService.getById(anyLong())).thenReturn(expected);
		mockMvc.perform(get("/orders/{id}", nextLong()))
			.andExpect(status().isOk())
			.andExpect(content().string(MAPPER.writeValueAsString(getResponse())))
		;
	}

	/**
	 * This part of the code is already covered by an integration test.
	 * This is only a demonstration of when unit tests are useless, when developers make wrong assumptions with their mock objects.
	 * @throws Exception
	 * @see OrderControllerIntegrationTest#testCreateOrder()
	 */
	@Test
	void testCreateOrder() throws Exception {
		when(mockOrderService.getById(anyLong())).thenReturn(getResponse());
		mockMvc.perform(post("/orders").contentType(APPLICATION_JSON).content("{}"))
			.andExpect(status().isCreated())
			.andExpect(content().string(MAPPER.writeValueAsString(getResponse())))
		;
	}

	private OrderTotalResponse getResponse() throws JsonProcessingException {
		var items = List.of(
			new OrderItemTotalResponse(new BigDecimal("1.00"), 6, BEERS),
			new OrderItemTotalResponse(new BigDecimal("2.00"), 3, BREADS),
			new OrderItemTotalResponse(valueOf(1.86), 1, VEGETABLES)
		);
		return new OrderTotalResponse(valueOf(4.86), items);
	}

}
