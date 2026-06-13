package com.example.orderapp.service;

import com.example.orderapp.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

	private static final Logger log = LoggerFactory.getLogger(OrderProducer.class);

	private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

	public OrderProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {

		this.kafkaTemplate = kafkaTemplate;
	}

	public void publish(OrderEvent orderEvent) {

		kafkaTemplate.send("order-topic", String.valueOf(orderEvent.getCustomerId()), orderEvent);

		log.info("Published Order : {}", orderEvent);
	}
}