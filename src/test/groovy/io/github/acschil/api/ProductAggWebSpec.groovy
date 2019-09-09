package io.github.acschil.api

import io.github.acschil.productPrice.ProductPriceCassandraDTO
import org.apache.http.client.HttpResponseException
import org.apache.http.client.fluent.Request
import org.springframework.core.io.ClassPathResource
import org.springframework.util.StreamUtils

import java.nio.charset.Charset

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo

class ProductAggWebSpec extends BaseWebSpec {

    def "fetch product aggregate data"() {
        setup:
        long id = 13860428
        cassandraAdminTemplate.insert(new ProductPriceCassandraDTO(productId: 13860428, value: 13.49))
        stubFor(get(urlEqualTo(getRedSkyProductDetailsURI(id)))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader('Content-Type', 'application/json')
                                .withBody(StreamUtils.copyToString(new ClassPathResource('product/sampleRedSkyProductDetailsResponse.json').getInputStream(), Charset.defaultCharset()))
                )
        )

        when:
        String result = Request.Get("${getAppBaseUri()}/products/${id}").execute().returnContent().asString()
        Map resultAsMap = getObjectMapper().readValue(result, Map)

        then:
        resultAsMap == [id: id, name: 'The Big Lebowski (Blu-ray)', current_price: [value: 13.49, currency_code: 'USD'], errors: []]
    }

    def "fetch product aggregate data - RedSky unavailable"() {
        setup:
        long id = 13860428
        cassandraAdminTemplate.insert(new ProductPriceCassandraDTO(productId: 13860428, value: 13.49))
        stubFor(get(urlEqualTo(getRedSkyProductDetailsURI(id)))
                .willReturn(
                        aResponse()
                                .withStatus(503)
                )
        )

        when:
        String result = Request.Get("${getAppBaseUri()}/products/${id}").execute().returnContent().asString()
        Map resultAsMap = getObjectMapper().readValue(result, Map)

        then:
        resultAsMap == [id: id, name: null, current_price: [value: 13.49, currency_code: 'USD'], errors: [[ProductDetailsFetchException: 'Could not get product details for id=13860428']]]
    }

    def "fetch product aggregate data - price does not exist"() {
        setup:
        long id = 13860428
        stubFor(get(urlEqualTo(getRedSkyProductDetailsURI(id)))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader('Content-Type', 'application/json')
                                .withBody(StreamUtils.copyToString(new ClassPathResource('product/sampleRedSkyProductDetailsResponse.json').getInputStream(), Charset.defaultCharset()))
                )
        )

        when:
        String result = Request.Get("${getAppBaseUri()}/products/${id}").execute().returnContent().asString()
        Map resultAsMap = getObjectMapper().readValue(result, Map)

        then:
        resultAsMap == [id: id, name: 'The Big Lebowski (Blu-ray)', current_price: null, errors: [[ProductPriceFetchException:'Could not get product pricing for id=13860428']]]
    }

    def "fetch product aggregate data - bad id"() {
        when:
        Request.Get("${getAppBaseUri()}/products/abc123Garbage").execute().returnContent().asString()

        then:
        HttpResponseException e = thrown(HttpResponseException)
        e.statusCode == 400
    }

}
