package br.com.project.example.adapter.inbound.web.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/example")
@RestController
class ExampleController {

    val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/v1/teste")
    fun example(): String {
        logger.info("Teste realizado com sucesso!!")

        return "Teste realizado com sucesso. "
    }

}