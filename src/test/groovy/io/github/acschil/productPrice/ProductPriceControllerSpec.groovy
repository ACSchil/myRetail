package io.github.acschil.productPrice

import spock.lang.Specification

class ProductPriceControllerSpec extends Specification {

    ProductPriceService productPriceServiceMock = Mock(ProductPriceService)
    ProductPriceController controller = new ProductPriceController(productPriceService: productPriceServiceMock)

    def "putPrice"() {
        setup:
        ProductPrice productPrice = new ProductPrice(value: 12.37)

        when:
        controller.putPrice(101, productPrice)

        then:
        1 * productPriceServiceMock.updateProductPrice(101, productPrice)
    }

}
