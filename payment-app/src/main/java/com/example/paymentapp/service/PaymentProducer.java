package com.example.paymentapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.paymentapp.dto.PaymentEvent;

import lombok.RequiredArgsConstructor;

@Service
public class PaymentProducer {

	private static final Logger log = LoggerFactory.getLogger(PaymentProducer.class);

	private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

	public PaymentProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {

		this.kafkaTemplate = kafkaTemplate;
	}

	public void publish(PaymentEvent event) {

		kafkaTemplate.send("payment-topic", String.valueOf(event.getCustomerId()), event);

		log.info("Published Payment : {}", event);
	}
}