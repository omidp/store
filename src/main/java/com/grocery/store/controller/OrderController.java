package com.grocery.store.controller;

import com.grocery.store.model.OrderRequest;
import com.grocery.store.model.OrderResponse;
import com.grocery.store.model.OrderTotalResponse;
import com.grocery.store.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;

	@GetMapping("/{id}")
	public ResponseEntity<OrderTotalResponse> getById(@PathVariable("id") Long orderId) {
		return ResponseEntity.ok(orderService.getById(orderId));
	}

	@GetMapping("/{id}/details")
	public ResponseEntity<OrderResponse> getDetailsById(@PathVariable("id") Long orderId) {
		return ResponseEntity.ok(orderService.getDetailsById(orderId));
	}

	@PostMapping
	public ResponseEntity<OrderTotalResponse> createOrder(@RequestBody OrderRequest orderRequest) {
		long orderId = orderService.insertOrderItem(orderRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.location(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(orderId).toUri())
			.body(orderService.getById(orderId));
	}

}
