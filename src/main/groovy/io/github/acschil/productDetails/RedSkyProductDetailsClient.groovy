package io.github.acschil.productDetails

import groovy.util.logging.Log4j2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Log4j2
@Component
class RedSkyProductDetailsClient implements ProductDetailsClient {

    private static final String queryString = '?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics'

    @Value('${product.details.redsky.baseUri}')
    private String redSkyBaseUri

    @Autowired
    RestTemplate restTemplate

    @Override
    Map getProductDetails(long id) {
        return restTemplate.getForObject("${redSkyBaseUri}/${id}${queryString}", Map) as Map
    }
}
