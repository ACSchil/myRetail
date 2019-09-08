package io.github.acschil.productAgg

import groovy.transform.EqualsAndHashCode
import io.github.acschil.productPrice.ProductPrice

@EqualsAndHashCode
class ProductAggData {

    Long id

    String name

    ProductPrice current_price

    List<String> errors = []

}
