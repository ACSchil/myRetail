package io.github.acschil.api

import io.github.acschil.productPrice.ProductPriceCassandraDTO
import org.apache.http.HttpResponse
import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType
import org.springframework.core.io.ClassPathResource
import org.springframework.data.cassandra.core.cql.CqlIdentifier
import org.springframework.util.StreamUtils
import spock.lang.Ignore
import spock.lang.Unroll

import java.nio.charset.Charset

class ProductPriceWebSpec extends BaseWebSpec {

    def "Update product price"() {
        setup:
        long id = 15117729
        String body = StreamUtils.copyToString(new ClassPathResource('price/sampleProductPricePutRequest.json').getInputStream(), Charset.defaultCharset())
        cassandraAdminTemplate.insert(new ProductPriceCassandraDTO(productId: id, value: 99.95))


        when:
        HttpResponse r = Request.Put("${getAppBaseUri()}/products/${id}")
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse()

        then:
        r.getStatusLine().statusCode == 204
        cassandraAdminTemplate.select('SELECT * FROM productPrice', ProductPriceCassandraDTO) == [new ProductPriceCassandraDTO(productId: id, value: 13.49)]
    }

    def "Add product price"() {
        setup:
        long id = 15117729
        String body = StreamUtils.copyToString(new ClassPathResource('price/sampleProductPricePutRequest.json').getInputStream(), Charset.defaultCharset())

        when:
        HttpResponse r = Request.Put("${getAppBaseUri()}/products/${id}")
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse()

        then:
        r.getStatusLine().statusCode == 204
        cassandraAdminTemplate.select('SELECT * FROM productPrice', ProductPriceCassandraDTO) == [new ProductPriceCassandraDTO(productId: id, value: 13.49)]
    }

    @Ignore // todo
    def "Add new product price - table did not exist"() {
        setup:
        long id = 15117729
        String body = StreamUtils.copyToString(new ClassPathResource('price/sampleProductPricePutRequest.json').getInputStream(), Charset.defaultCharset())
        cassandraAdminTemplate.dropTable(true, CqlIdentifier.of('productPrice'))

        when:
        HttpResponse r = Request.Put("${getAppBaseUri()}/products/${id}")
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse()

        then:
        r.getStatusLine().statusCode == 204
        cassandraAdminTemplate.select('SELECT * FROM productPrice', ProductPriceCassandraDTO) == [new ProductPriceCassandraDTO(productId: id, value: 13.49)]
    }

    def "Update product price - bad id"() {
        setup:
        String id = 'abc123'
        String body = StreamUtils.copyToString(new ClassPathResource('price/sampleProductPricePutRequest.json').getInputStream(), Charset.defaultCharset())

        when:
        HttpResponse r = Request.Put("${getAppBaseUri()}/products/${id}")
                .bodyString(body, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse()

        then:
        r.getStatusLine().statusCode == 400
        cassandraAdminTemplate.select('SELECT * FROM productPrice', ProductPriceCassandraDTO).size() == 0
    }

    @Unroll
    def "Update product price - malformed body=#json"() {
        setup:
        long id = 15117729

        when:
        HttpResponse r = Request.Put("${getAppBaseUri()}/products/${id}")
                .bodyString(json, ContentType.APPLICATION_JSON)
                .execute()
                .returnResponse()

        then:
        r.getStatusLine().statusCode == 400
        cassandraAdminTemplate.select('SELECT * FROM productPrice', ProductPriceCassandraDTO).size() == 0

        where:
        json << [
                '',
                '{}',
                'plain-text',
                '{"price": 57.09}',
                '{"value": 1234567890987654321.00}',
                '{"value": 55.113}'
        ]
    }

}
