package io.github.acschil.productAgg

import io.github.acschil.productDetails.ProductDetails
import io.github.acschil.productDetails.ProductDetailsService
import io.github.acschil.productPrice.ProductPrice
import io.github.acschil.productPrice.ProductPriceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductAggService {

    @Autowired
    private ProductDetailsService productDetailsService

    @Autowired
    private ProductPriceService productPriceService

    ProductAggData getProduct(long id) {
        ProductDetails productDetails

        // todo async calls
        try {
            productDetails = productDetailsService.getProductDetails(id)
        } catch (Exception e) {
            // todo handle exceptions
        }

        ProductPrice productPrice
        try {
            productPrice = productPriceService.getProductPrice(id)
        } catch (Exception e) {
            // todo handle exceptions
        }

        // todo get price
        return new ProductAggData(id: id, name: productDetails?.name, current_price: productPrice)
    }

}
