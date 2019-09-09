package io.github.acschil.api

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import io.github.acschil.productPrice.ProductPriceCassandraDTO
import io.github.acschil.webapp.Application
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.cassandra.core.CassandraAdminTemplate
import org.springframework.data.cassandra.core.cql.CqlIdentifier
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ActiveProfiles('test')
@ContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BaseWebSpec extends Specification {

    private WireMockServer wireMockServer

    private Session session

    private ObjectMapper objectMapper

    private static final String applicationPort = 8080

    private static final String keyspaceName = 'myRetailTest'

    private static int stubPort = 8181

    @Autowired
    private CassandraAdminTemplate adminTemplate

    void setupSpec() {
        WireMock.configureFor(stubPort)
        getWireMockServer().start()
    }

    void setup() {
        adminTemplate.dropTable(true, CqlIdentifier.of('productPrice'))
        adminTemplate.createTable(true, CqlIdentifier.of('productPrice'), ProductPriceCassandraDTO, [:])
    }

    void cleanupSpec() {
        getWireMockServer().stop()
        getCassandraSession().close()
    }

    void cleanup() {
        WireMock.reset()
        adminTemplate.dropTable(true, CqlIdentifier.of('productPrice'))
    }

    String getAppBaseUri() {
        return "http://localhost:${applicationPort}"
    }

    String getRedSkyProductDetailsURI(long id) {
        return "/v2/pdp/tcin/${id}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics"
    }

    CassandraAdminTemplate getCassandraAdminTemplate() {
        return adminTemplate
    }

    ObjectMapper getObjectMapper() {
        if (!objectMapper) {
            objectMapper = new ObjectMapper()
        }
        return objectMapper
    }

    private getCassandraSession() {
        if (!session) {
            Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").withPort(9042).build()
            session = cluster.connect()
            session.execute("CREATE KEYSPACE IF NOT EXISTS ${keyspaceName} WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '1' };")
            session.execute("USE ${keyspaceName};")
        }
        return session
    }

    private getWireMockServer() {
        if (!wireMockServer) {
            wireMockServer = new WireMockServer(stubPort)
        }
        return wireMockServer
    }

}
