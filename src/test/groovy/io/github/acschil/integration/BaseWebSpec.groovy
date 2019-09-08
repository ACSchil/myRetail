package io.github.acschil.integration

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Session
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

    private static final String applicationPort = 8080

    private static final String keyspaceName = 'myRetailTest'

    private static int stubPort = 8181

    private static final String redskyBaseUri = "http://localhost:${stubPort}/v2/pdp/tcin/"

    @Autowired
    private CassandraAdminTemplate adminTemplate

    void setupSpec() {
        WireMock.configureFor(stubPort)
        getWireMockServer().start()
    }

    void setup() {
        adminTemplate.createTable(true, CqlIdentifier.of('productPrice'), ProductPriceCassandraDTO, [:])
    }

    void cleanupSpec() {
        getWireMockServer().stop()
        getCassandraSession().close()
    }

    void cleanup() {
        WireMock.reset()
        adminTemplate.dropTable(CqlIdentifier.of('productPrice'));
    }

    String getAppBaseUri() {
        return "http://localhost:${applicationPort}"
    }

    String getRedSkyBaseUri() {
        return redskyBaseUri
    }

    CassandraAdminTemplate getCassandraAdminTemplate() {
        return adminTemplate
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
