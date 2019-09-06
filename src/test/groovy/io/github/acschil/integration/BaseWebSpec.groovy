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
    private String port

//    private static WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8181))
//
//    void setupSpec() {
//        wireMockServer.start()
//    }
//
//    void cleanup() {
//        WireMock.reset()
//    }
//
//    void cleanupSpec() {
//        wireMockServer.stop()
//    }

    String getAppBaseUri() {
        return "http://localhost:${port}"
    }

}
