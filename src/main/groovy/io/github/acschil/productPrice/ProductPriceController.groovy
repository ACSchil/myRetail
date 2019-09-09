package io.github.acschil.productPrice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

@RestController
class ProductPriceController {

    @Autowired
    ProductPriceService productPriceService

    @PutMapping(path = '/products/{id}', consumes = 'application/json')
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void putPrice(@PathVariable long id, @RequestBody @Valid ProductPrice newPrice) {
        productPriceService.updateProductPrice(id, newPrice)
    }

}
