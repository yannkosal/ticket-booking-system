package com.kosal.orderservice;

import com.kosal.bookingservice.event.BookingEvent;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class KafkaJsonDeserializerTests {

	@Test
	void canConstructKafkaJsonDeserializer() {
		assertDoesNotThrow(() -> new JsonDeserializer<>(BookingEvent.class));
	}
}
