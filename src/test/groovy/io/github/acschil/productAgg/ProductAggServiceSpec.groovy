package io.github.acschil.productAgg

import io.github.acschil.productDetails.ProductDetails
import io.github.acschil.productDetails.ProductDetailsFetchException
import io.github.acschil.productDetails.ProductDetailsService
import io.github.acschil.productPrice.ProductPrice
import io.github.acschil.productPrice.ProductPriceFetchException
import io.github.acschil.productPrice.ProductPriceService
import spock.lang.Specification

class ProductAggServiceSpec extends Specification {

    ProductDetailsService productDetailsServiceMock = Mock(ProductDetailsService)
    ProductPriceService productPriceServiceMock = Mock(ProductPriceService)
    ProductAggService service = new ProductAggService(
            productDetailsService: productDetailsServiceMock,
            productPriceService: productPriceServiceMock
    )

    def "getProduct"() {
        setup:
        ProductDetails productDetails = new ProductDetails(name: 'Dragon Ball Z')
        ProductPrice productPrice = new ProductPrice(value: 3.99)
        ProductAggData expectedProductAggData = new ProductAggData(
                id: 9001,
                name: 'Dragon Ball Z',
                current_price: productPrice,
                errors: []
        )

        when:
        ProductAggData result = service.getProduct(9001)

        then:
        result == expectedProductAggData

        1 * productDetailsServiceMock.getProductDetails(9001) >> productDetails
        1 * productPriceServiceMock.getProductPrice(9001) >> productPrice
    }

    def "getProduct - cannot get details"() {
        setup:
        ProductPrice productPrice = new ProductPrice(value: 3.99)
        ProductAggData expectedProductAggData = new ProductAggData(
                id: 9001,
                name: null,
                current_price: productPrice,
                errors: [['ProductDetailsFetchException': 'Product Details service is down.']]
        )

        when:
        ProductAggData result = service.getProduct(9001)

        then:
        result == expectedProductAggData

        1 * productDetailsServiceMock.getProductDetails(9001) >> { throw new ProductDetailsFetchException('Product Details service is down.') }
        1 * productPriceServiceMock.getProductPrice(9001) >> productPrice
    }

    def "getProduct - cannot get price"() {
        setup:
        ProductDetails productDetails = new ProductDetails(name: 'Dragon Ball Z')
        ProductAggData expectedProductAggData = new ProductAggData(
                id: 9001,
                name: 'Dragon Ball Z',
                current_price: null,
                errors: [['ProductPriceFetchException': 'Table does not exist']]
        )

        when:
        ProductAggData result = service.getProduct(9001)

        then:
        result == expectedProductAggData

        1 * productDetailsServiceMock.getProductDetails(9001) >> productDetails
        1 * productPriceServiceMock.getProductPrice(9001) >> { throw new ProductPriceFetchException('Table does not exist') }
    }

    def "getProduct - cannot get price or details"() {
        setup:
        ProductAggData expectedProductAggData = new ProductAggData(
                id: 9001,
                name: null,
                current_price: null,
                errors: [['ProductDetailsFetchException': 'Product Details service is down.'], ['ProductPriceFetchException': 'Table does not exist']]
        )

        when:
        ProductAggData result = service.getProduct(9001)

        then:
        result == expectedProductAggData

        1 * productDetailsServiceMock.getProductDetails(9001) >> { throw new ProductDetailsFetchException('Product Details service is down.') }
        1 * productPriceServiceMock.getProductPrice(9001) >> { throw new ProductPriceFetchException('Table does not exist') }
    }
}
