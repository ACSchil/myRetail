package io.github.acschil.productPrice

import groovy.transform.EqualsAndHashCode

import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

@EqualsAndHashCode
class ProductPrice {

    @NotNull
    @Digits(integer=15, fraction=2)
    @DecimalMin(value = '0.00', inclusive = true)
    BigDecimal value

    String currency_code = ProductPriceConstants.USD_CURRENCY_CODE

}
