package io.github.acschil.productAgg

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductAggController {
    @Autowired
    ProductAggService productAggService

    @GetMapping(path = '/products/{id}', produces = 'application/json')
    @ResponseBody ProductAggData getProduct(@PathVariable long id) {
        return  productAggService.getProduct(id)
    }
}
