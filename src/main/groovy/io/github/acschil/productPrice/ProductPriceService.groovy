package io.github.acschil.productPrice

import groovy.util.logging.Log4j2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Log4j2
@Service
class ProductPriceService {

    @Autowired
    ProductPriceRepository priceRepository

    void updateProductPrice(long id, ProductPrice newPrice) {
        ProductPriceCassandraDTO dto = new ProductPriceCassandraDTO(productId: id, value: newPrice.value)
        priceRepository.save(dto)
    }

    ProductPrice getProductPrice(long id) {
        try {
            ProductPriceCassandraDTO dto = priceRepository.findById(id).get()
            return new ProductPrice(value: dto.value)
        } catch (Exception e) {
            log.error("Failed to fetch price for product id=${id}, msg=${e.getMessage()}")
            throw new ProductPriceFetchException("Could not get product pricing for id=${id}")
        }
    }

}
