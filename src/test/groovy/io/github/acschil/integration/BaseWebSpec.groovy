package io.github.acschil.integration

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import io.github.acschil.productPrice.ProductPrice
import io.github.acschil.productPrice.ProductPriceCassandraDTO
import io.github.acschil.webapp.Application
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.cassandra.core.CassandraAdminOperations
import org.springframework.data.cassandra.core.cql.CqlIdentifier
import spock.lang.Specification

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BaseWebSpec extends Specification {

    private static final int STUB_PORT = 8181

    private static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS myRetail " + "WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };";

    private static final String KEYSPACE_ACTIVATE_QUERY = "USE myRetail;";

    private static final WireMockServer wireMockServer = new WireMockServer(STUB_PORT)

    private static Session session

    @Value('${server.port}')
    private String applicationPort

    @Autowired
    private CassandraAdminOperations adminTemplate;

    void setupSpec() {
        WireMock.configureFor(STUB_PORT)
        wireMockServer.start()

        Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").withPort(9042).build()
        session = cluster.connect()
        session.execute(KEYSPACE_CREATION_QUERY)
        session.execute(KEYSPACE_ACTIVATE_QUERY)
    }

    void setup() {
        adminTemplate.createTable(true, CqlIdentifier.of('productPrice'), ProductPriceCassandraDTO, [:])
    }

    void cleanupSpec() {
        wireMockServer.stop()
        session.close()
    }

    void cleanup() {
        WireMock.reset()

        adminTemplate.dropTable(CqlIdentifier.of('productPrice'));
    }

    String getAppBaseUri() {
        return "http://localhost:${applicationPort}"
    }

}
