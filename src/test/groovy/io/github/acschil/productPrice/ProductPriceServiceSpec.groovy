package io.github.acschil.productPrice

import spock.lang.Specification

class ProductPriceServiceSpec extends Specification {

    ProductPriceRepository priceRepositoryMock = Mock(ProductPriceRepository)
    ProductPriceService service = new ProductPriceService(priceRepository: priceRepositoryMock)

    def "updateProductPrice"() {
        setup:
        ProductPrice price = new ProductPrice(value: 11.01)
        ProductPriceCassandraDTO dto = new ProductPriceCassandraDTO(productId: 117, value: 11.01)

        when:
        service.updateProductPrice(117, price)

        then:
        1 * priceRepositoryMock.save(dto)
    }

    def "getProductPrice"() {
        setup:
        ProductPriceCassandraDTO dto = new ProductPriceCassandraDTO(productId: 117, value: 11.01)
        Optional<ProductPriceCassandraDTO> expected = Optional.of(dto)

        when:
        ProductPrice result = service.getProductPrice(117)

        then:
        result.value == 11.01
        result.currency_code == 'USD'

        1 * priceRepositoryMock.findById(117) >> expected
    }

    def "getProductPrice - failure"() {
        when:
        service.getProductPrice(117)

        then:
        thrown(ProductPriceFetchException)

        1 * priceRepositoryMock.findById(117) >> { throw new RuntimeException('uh oh...') }
    }

}
