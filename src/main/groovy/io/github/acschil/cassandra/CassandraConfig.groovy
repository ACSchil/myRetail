package io.github.acschil.cassandra

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories

@Configuration
@EnableCassandraRepositories(basePackages = "io.github.acschil.productPrice")
class CassandraConfig extends AbstractCassandraConfiguration {

    @Value('${cassandra.contactPoints}')
    private String contactPoints

    @Value('${cassandra.port}')
    private int port

    @Value('${cassandra.keyspaceName}')
    private String keyspaceName

    @Override
    protected String getKeyspaceName() {
        return keyspaceName
    }

    @Bean
    CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster =
                new CassandraClusterFactoryBean()
        cluster.setContactPoints(contactPoints)
        cluster.setPort(port)
        return cluster
    }

    @Bean
    CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        return new CassandraMappingContext()
    }

    // todo failure to connect should not kill the application!
}
