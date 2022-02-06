package br.com.project.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class ExampleApplication

fun main(args: Array<String>) {
	runApplication<ExampleApplication>(*args)
}
