package io.github.acschil.productDetails

import spock.lang.Specification

class ProductDetailsConfigSpec extends Specification {

    ProductDetailsConfig config = new ProductDetailsConfig()

    def "productDetailsClient creates a RedSkyProductDetailsClient"() {
        expect:
        config.productDetailsClient() instanceof RedSkyProductDetailsClient
    }

}
