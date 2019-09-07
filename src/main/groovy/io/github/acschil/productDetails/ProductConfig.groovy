package io.github.acschil.productDetails

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProductConfig {
    @Bean
    ProductDetailsClient productDetailsClient() {
        return new RedSkyProductDetailsClient()
    }
}
