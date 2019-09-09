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
    ProductDetailsService productDetailsService

    @Autowired
    ProductPriceService productPriceService

    ProductAggData getProduct(long id) {

        List<Map<String, String>> errors = []

        ProductDetails productDetails
        try {
            productDetails = productDetailsService.getProductDetails(id)
        } catch (Exception e) {
            errors.add([(e.getClass().simpleName): e.getMessage()])
        }

        ProductPrice productPrice
        try {
            productPrice = productPriceService.getProductPrice(id)
        } catch (Exception e) {
            errors.add([(e.getClass().simpleName): e.getMessage()])
        }

        return new ProductAggData(id: id, name: productDetails?.name, current_price: productPrice, errors: errors)
    }

}
