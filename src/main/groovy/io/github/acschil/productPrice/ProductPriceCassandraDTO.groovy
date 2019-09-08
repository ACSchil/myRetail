package io.github.acschil.productPrice

import groovy.transform.EqualsAndHashCode
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table('productPrice')
@EqualsAndHashCode
class ProductPriceCassandraDTO {

    @PrimaryKey
    Long productId

    BigDecimal price

}
