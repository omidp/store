package com.grocery.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery.store.IntegrationTestContextConfig;
import com.grocery.store.model.OrderItemTotalResponse;
import com.grocery.store.model.OrderTotalResponse;
import com.grocery.store.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static com.grocery.store.domain.Category.BEERS;
import static com.grocery.store.domain.Category.BREADS;
import static com.grocery.store.domain.Category.VEGETABLES;
import static java.math.BigDecimal.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = IntegrationTestContextConfig.class)
@TestPropertySource(locations = "classpath*:/application.properties")
public class OrderControllerIntegrationTest {

	private MockMvc mockMvc;
	private final ObjectMapper MAPPER = new ObjectMapper();

	@Autowired
	private OrderService orderService;

	@Autowired
	private IntegrationTestContextConfig.OrderId orderId;

	private OrderController orderController;

	@BeforeEach
	void setup() {
		orderController = new OrderController(orderService);
		mockMvc = MockMvcBuilders.standaloneSetup(orderController)
			.setControllerAdvice(new RestExceptionController())
			.build();
	}

	@Test
	void testGetById() throws Exception {
		mockMvc.perform(get("/orders/{id}", orderId.getIdentifier()))
			.andExpect(status().isOk())
			.andExpect(content().string(getResponse()))
		;

	}

	@Test
	void testCreateOrder() throws Exception {
		var requestBody = """
			{
			    "items":[
			        {
			            "productId":11,
			            "qty":1
			        },
			        {
			            "productId":10,
			            "qty":1
			        },
			        {
			            "productId":16,
			            "qty":1,
			            "attributes":{"WEIGHT":"200"}
			        },
			        {
			            "productId":14,
			            "qty":6           
			        }
			    ]
			}
			""";
		mockMvc.perform(post("/orders").contentType(APPLICATION_JSON).content(requestBody))
			.andExpect(status().isCreated())
			.andExpect(content().string(getResponse()))
		;

	}

	@Test
	void testCreateOrderQtyValidation() throws Exception {
		var requestBody = """
			{
			    "items":[
			        {
			            "productId":11,
			            "qty":0
			        }
			    ]
			}
			""";
		mockMvc.perform(post("/orders").contentType(APPLICATION_JSON).content(requestBody))
			.andExpect(status().isBadRequest())
			.andExpect(content().string("[{\"code\":0,\"message\":\"items[0].qty must be greater than or equal to 1\"}]"))
		;

	}

	private String getResponse() throws JsonProcessingException {
		var items = List.of(
			new OrderItemTotalResponse(new BigDecimal("1.00"), 6, BEERS),
			new OrderItemTotalResponse(new BigDecimal("2.00"), 3, BREADS),
			new OrderItemTotalResponse(valueOf(1.86), 1, VEGETABLES)
		);
		return MAPPER.writeValueAsString(new OrderTotalResponse(valueOf(4.86), items));
	}

}
