package io.github.acschil.webapp

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@ComponentScan([
    'io.github.acschil.productAgg',
    'io.github.acschil.productDetails',
    'io.github.acschil.productPrice',
    'io.github.acschil.webapp'
])
@SpringBootApplication
class Application {

    static void main(String[] args) {
        SpringApplication.run(Application.class, args)
    }

}