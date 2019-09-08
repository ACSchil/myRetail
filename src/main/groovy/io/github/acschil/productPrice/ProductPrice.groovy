package io.github.acschil.productPrice

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class ProductPrice {

    BigDecimal price

    String currency = ProductPriceConstants.USD_CURRENCY_CODE

}
