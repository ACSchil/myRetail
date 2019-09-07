package io.github.acschil.productPrice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductPriceService  {

    @Autowired
    ProductPriceRepository priceRepository

    void updateProductPrice(long id, ProductPrice newPrice) {
        ProductPriceCassandraDTO dto = new ProductPriceCassandraDTO(productId: id, price: newPrice.price)
        priceRepository.save(dto)
    }

    ProductPrice getProductPrice(long id) {
        ProductPriceCassandraDTO dto = priceRepository.findById(id).get()
        return new ProductPrice(price: dto.price)
    }

}
