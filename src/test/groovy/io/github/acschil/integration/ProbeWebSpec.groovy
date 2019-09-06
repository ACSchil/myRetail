package io.github.acschil.integration

import org.apache.http.client.fluent.Request

class ProbeWebSpec extends BaseWebSpec {

    def "application's liveness check responds with 'Alive'"() {
        when:
        String livenessResult = Request.Get(getAppBaseUri() + '/liveness').execute().returnContent().asString()

        then:
        livenessResult == 'Alive'
    }

}
