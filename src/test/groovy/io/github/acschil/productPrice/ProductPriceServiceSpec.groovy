package io.github.acschil.productPrice

import spock.lang.Specification

class ProductPriceServiceSpec extends Specification {

    ProductPriceRepository priceRepositoryMock = Mock(ProductPriceRepository)
    ProductPriceService service = new ProductPriceService(priceRepository: priceRepositoryMock)

    def "updateProductPrice"() {
        setup:
        ProductPrice price = new ProductPrice(price: 11.01)
        ProductPriceCassandraDTO dto = new ProductPriceCassandraDTO(productId: 117, price: 11.01)

        when:
        service.updateProductPrice(117, price)

        then:
        1 * priceRepositoryMock.save(dto)
    }

    def "getProductPrice"() {
        setup:
        ProductPrice price = new ProductPrice(price: 11.01)
        ProductPriceCassandraDTO dto = new ProductPriceCassandraDTO(productId: 117, price: 11.01)
        Optional<ProductPriceCassandraDTO> expected = Optional.of(dto)

        when:
        ProductPrice productPrice = service.getProductPrice(117)

        then:
        productPrice.price == 11.01

        1 * priceRepositoryMock.findById(117) >> expected
    }

}
