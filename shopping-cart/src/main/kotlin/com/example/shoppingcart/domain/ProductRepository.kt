package com.example.shoppingcart.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface ProductRepository : ReactiveMongoRepository<Product, String> {
    fun existsByName(name: String): Mono<Boolean>
}
