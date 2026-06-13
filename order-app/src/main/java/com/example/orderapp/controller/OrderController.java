package com.example.orderapp.controller;

import com.example.orderapp.dto.OrderEvent;
import com.example.orderapp.service.OrderProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderProducer producer;

	public OrderController(OrderProducer producer) {
		this.producer = producer;
	}

	@PostMapping
	public String createOrder(@RequestParam Integer customerId, @RequestParam String product) {

		OrderEvent orderEvent = new OrderEvent();

		orderEvent.setCustomerId(customerId);
		orderEvent.setProduct(product);

		orderEvent.setOrderId(ThreadLocalRandom.current().nextInt(1000, 9999));

		producer.publish(orderEvent);

		return "Order Published Successfully";
	}
}