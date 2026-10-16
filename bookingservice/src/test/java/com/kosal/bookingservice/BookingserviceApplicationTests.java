package com.kosal.bookingservice;

import com.kosal.bookingservice.event.BookingEvent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonSerializer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class BookingserviceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void kafkaJsonSerializerCanBeConstructed() {
		assertDoesNotThrow(() -> new JsonSerializer<BookingEvent>());
	}
}
