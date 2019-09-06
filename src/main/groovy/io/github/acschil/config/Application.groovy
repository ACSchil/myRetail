package io.github.acschil.config

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan([
    'io.github.acschil.controller',
    'io.github.acschil.service'
])
class Application {

    static void main(String[] args) {
        SpringApplication.run(Application.class, args)
    }

}