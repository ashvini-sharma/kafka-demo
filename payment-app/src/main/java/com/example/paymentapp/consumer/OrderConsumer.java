package com.example.paymentapp.consumer;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.paymentapp.dto.OrderEvent;
import com.example.paymentapp.dto.PaymentEvent;
import com.example.paymentapp.service.PaymentProducer;

@Component
public class OrderConsumer {

	private final PaymentProducer paymentProducer;
	private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);

	public OrderConsumer(PaymentProducer paymentProducer) {
		super();
		this.paymentProducer = paymentProducer;
	}

	@KafkaListener(topics = "order-topic")
	public void consume(OrderEvent orderEvent) {

		log.info("Received Order for payment processing : {}", orderEvent);

		PaymentEvent paymentEvent = new PaymentEvent();

		paymentEvent.setCustomerId(orderEvent.getCustomerId());

		paymentEvent.setOrderId(orderEvent.getOrderId());

		paymentEvent.setPrice(ThreadLocalRandom.current().nextInt(500, 5000));

		paymentProducer.publish(paymentEvent);
	}

}