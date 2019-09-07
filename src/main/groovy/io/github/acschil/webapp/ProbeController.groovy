package io.github.acschil.webapp

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/probe')
class ProbeController {
    @GetMapping('/liveness')
    String livenessProbe() {
        return 'Alive'
    }

    @GetMapping('/readiness')
    String readinessProbe() {
        // todo hit https://redsky.target.com/heartbeat
        // todo connect to cassandra cluster
        return 'Ready'
    }
}
