package br.com.project.example.configuration

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.context.annotation.Profile
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Profile("test")
@Testcontainers
open class MongoDBContainerTest {

    @Container
    private val mongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))

    @BeforeAll
    fun setUp(){
        mongoDBContainer.start()
    }

    @AfterAll
    fun finish(){
        mongoDBContainer.stop()
    }

}