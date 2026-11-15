package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:hotpot-test;MODE=MySQL;DB_CLOSE_DELAY=-1")
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
