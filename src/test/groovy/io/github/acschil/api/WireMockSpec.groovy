package io.github.acschil.api

import org.apache.http.client.fluent.Request
import spock.lang.Ignore

import static com.github.tomakehurst.wiremock.client.WireMock.*

class WireMockSpec extends BaseWebSpec {
    @Ignore
    def "wire mock sanity check"() {
        setup:
        stubFor(get(urlEqualTo('/hello/world'))
                .willReturn(
                        aResponse()
                                .withHeader('Content-Type', 'text/plain')
                                .withBody('Hello World!')
                )
        )

        when:
        String result = Request.Get('http://localhost:8181/hello/world').execute().returnContent().asString()

        then:
        result == 'Hello World!'
    }
}
