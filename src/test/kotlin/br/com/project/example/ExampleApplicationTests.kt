package br.com.project.example

import br.com.project.example.configuration.MongoDBContainerTest
import org.junit.ClassRule
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExampleApplicationTests: MongoDBContainerTest() {



	@Test
	fun contextLoads() {
		assertAll(
			{ assertTrue(true) }
		)
	}

}
