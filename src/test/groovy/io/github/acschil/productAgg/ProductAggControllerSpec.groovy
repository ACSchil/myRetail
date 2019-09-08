package io.github.acschil.productAgg

import spock.lang.Specification

class ProductAggControllerSpec extends Specification {

    ProductAggService productAggServiceMock = Mock(ProductAggService)
    ProductAggController controller = new ProductAggController(productAggService: productAggServiceMock)

    def "getProduct"() {
        setup:
        ProductAggData expected = new ProductAggData()

        when:
        ProductAggData result =  controller.getProduct(7L)

        then:
        result == expected
        1 * productAggServiceMock.getProduct(7L) >> expected
    }

}
