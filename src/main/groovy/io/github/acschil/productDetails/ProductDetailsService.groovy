package io.github.acschil.productDetails

import groovy.util.logging.Log4j2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException

@Log4j2
@Service
class ProductDetailsService {

    @Autowired
    ProductDetailsClient productDetailsClient

    ProductDetails getProductDetails(long id) {
        Map productDetailsResponse
        try {
            productDetailsResponse = productDetailsClient.getProductDetails(id)
        } catch (HttpClientErrorException e) {
            log.info("Failed to fetch details for product id=${id}, statusCode=${e.statusCode}")
        } catch (Exception e) {
            log.error("Failed to fetch details for product id=${id}, msg=${e.getMessage()}")
        }

        String productName = productDetailsResponse?.get('product')?.get('item')?.get('product_description')?.get('title')
        if (!productName) {
            log.error("Could not get product namge for id=${id}, response=${productDetailsResponse}")
            throw new ProductDetailsFetchException("Could not get product details for id=${id}")
        }

        return new ProductDetails(name: productName)
    }

}
