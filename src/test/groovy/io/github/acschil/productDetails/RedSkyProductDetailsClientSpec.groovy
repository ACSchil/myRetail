package io.github.acschil.productDetails

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class RedSkyProductDetailsClientSpec extends Specification {

    RestTemplate restTemplateMock = Mock(RestTemplate)
    RedSkyProductDetailsClient client = new RedSkyProductDetailsClient(
            redSkyBaseUri: 'http://uri/maybe/a/version/too',
            restTemplate: restTemplateMock
    )

    ObjectMapper objectMapper = new ObjectMapper()
    Resource resource = new ClassPathResource('product/sampleRedSkyProductDetailsResponse.json')
    Map productDetailsResponse = objectMapper.readValue(resource.getFile(), Map)

    def "getProductDetails"() {
        when:
        Map result = client.getProductDetails(101)

        then:
        result == productDetailsResponse

        1 * restTemplateMock.getForObject(
                'http://uri/maybe/a/version/too/101?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics',
                Map
        ) >> productDetailsResponse
    }

}
