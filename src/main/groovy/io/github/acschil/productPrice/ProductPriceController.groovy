package io.github.acschil.productPrice

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus

class ProductPriceController {
    @PutMapping(path = '/products/{id}', consumes = 'application/json')
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void putPrice(@PathVariable long id, @RequestBody ProductPrice newPrice) {

    }
}
