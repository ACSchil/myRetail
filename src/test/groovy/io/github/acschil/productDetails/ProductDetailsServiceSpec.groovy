package io.github.acschil.productDetails

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import spock.lang.Specification
import spock.lang.Unroll

class ProductDetailsServiceSpec extends Specification {

    ProductDetailsClient productDetailsClientMock = Mock(ProductDetailsClient)
    ProductDetailsService service = new ProductDetailsService(productDetailsClient: productDetailsClientMock)

    ObjectMapper objectMapper = new ObjectMapper()
    Resource resource = new ClassPathResource('product/sampleRedSkyProductDetailsResponse.json')
    Map productDetailsResponse = objectMapper.readValue(resource.getFile(), Map)

    def "getProductDetails"() {
        setup:
        ProductDetails expected = new ProductDetails(name: 'The Big Lebowski (Blu-ray)')

        when:
        ProductDetails result = service.getProductDetails(101)

        then:
        result == expected

        1 * productDetailsClientMock.getProductDetails(101) >> productDetailsResponse
    }

    def "getProductDetails - fetch failed"() {
        when:
        service.getProductDetails(101)

        then:
        thrown(ProductDetailsFetchException)

        1 * productDetailsClientMock.getProductDetails(101) >> {
            throw new RuntimeException('yer outta yer element Donnie!')
        }
    }

    @Unroll
    def "getProductDetails - response structure changed from under our feet, e.g. #productDetailsResponse"() {
        when:
        service.getProductDetails(101)

        then:
        thrown(ProductDetailsFetchException)

        1 * productDetailsClientMock.getProductDetails(101) >> productDetailsResponse

        where:
        productDetailsResponse << [
                [title: 'moved to top level'],
                [product: [title: 'hid it here']],
                [product: [item: [title: 'hid it here']]],
                [product: [item: [product_description: [name: 'calling the field name now']]]]
        ]
    }

}
