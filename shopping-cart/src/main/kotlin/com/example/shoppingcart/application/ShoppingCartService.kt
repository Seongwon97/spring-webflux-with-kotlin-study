package com.example.shoppingcart.application

import com.example.shoppingcart.application.dto.ShoppingCartRequest
import com.example.shoppingcart.domain.ProductRepository
import com.example.shoppingcart.domain.ShoppingCart
import com.example.shoppingcart.domain.ShoppingCartRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

@Transactional(readOnly = true)
@Service
class ShoppingCartService(
    private val productRepository: ProductRepository,
    private val shoppingCartRepository: ShoppingCartRepository
) {
    @Transactional
    fun register(request: ShoppingCartRequest): Mono<ShoppingCart> {
        return request.items.keys.toFlux()
            .flatMap { productName -> verifyExistProduct(productName) }
            .then(ShoppingCart(request.items).toMono())
            .flatMap { shoppingCart -> shoppingCartRepository.save(shoppingCart) }
    }

    private fun verifyExistProduct(productName: String) = productRepository.existsByName(productName)
        .map { isExist ->
            if (!isExist)
                throw IllegalArgumentException()
        }
}
