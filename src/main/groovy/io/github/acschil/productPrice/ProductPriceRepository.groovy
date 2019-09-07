package io.github.acschil.productPrice

import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductPriceRepository extends CassandraRepository<ProductPriceCassandraDTO, Long> {

}
