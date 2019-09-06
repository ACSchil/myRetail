package io.github.acschil.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProbeController {

    @RequestMapping("/liveness")
    String livenessProbe() {
        return "Alive";
    }

    @RequestMapping("/readiness")
    String readinessProbe() {
        return "Ready";
    }

}
