package io.github.acschil.api

import org.apache.http.client.fluent.Request

class ProbeWebSpec extends BaseWebSpec {

    def "application's liveness check responds with 'Alive'"() {
        when:
        String result = Request.Get(getAppBaseUri() + '/probe/liveness').execute().returnContent().asString()

        then:
        result == 'Alive'
    }

    def "application's readiness check responds with 'Ready'"() {
        when:
        String result = Request.Get(getAppBaseUri() + '/probe/readiness').execute().returnContent().asString()

        then:
        result == 'Ready'
    }

}
