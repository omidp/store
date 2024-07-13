package com.grocery.store.controller;

import com.grocery.store.model.ProductResponse;
import com.grocery.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	@GetMapping("")
	public ResponseEntity<List<ProductResponse>> get() {
		return ResponseEntity.ok(productService.get());
	}

}
