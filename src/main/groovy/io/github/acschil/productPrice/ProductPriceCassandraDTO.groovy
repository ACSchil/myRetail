package io.github.acschil.productPrice

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table('productPrice')
class ProductPriceCassandraDTO {

    @PrimaryKey
    Long productId

    BigDecimal price

}
