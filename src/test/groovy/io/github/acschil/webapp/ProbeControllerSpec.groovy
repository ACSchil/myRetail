package io.github.acschil.webapp

import spock.lang.Specification

class ProbeControllerSpec extends Specification {

    ProbeController controller = new ProbeController()

    def "livenessProbe"() {
        expect:
        controller.livenessProbe() == 'Alive'
    }

    def "readinessProbe"() {
        expect:
        controller.readinessProbe() == 'Ready'
    }

}
