package io.github.acschil.integration

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import io.github.acschil.config.Application
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BaseWebSpec extends Specification {

    @Value('${server.port}')
    private String applicationPort

    private static final int STUB_PORT = 8181

    private static WireMockServer wireMockServer = new WireMockServer(STUB_PORT)

    void setupSpec() {
        WireMock.configureFor(STUB_PORT)
        wireMockServer.start()
    }

    void cleanup() {
        WireMock.reset()
    }

    void cleanupSpec() {
        wireMockServer.stop()
    }

    String getAppBaseUri() {
        return "http://localhost:${applicationPort}"
    }

}
