package com.example.shoppingcart.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import reactor.kotlin.test.test

@DataMongoTest
class ShoppingCartRepositoryTest @Autowired constructor(
    private val shoppingCartRepository: ShoppingCartRepository
) {

    @Test
    fun saveShoppingCart() {
        val shoppingCart = ShoppingCart(mapOf("딸기 우유" to 2, "포카칩" to 1))
        val shoppingCartMono = shoppingCartRepository.save(shoppingCart)

        shoppingCartMono.test()
            .assertNext {
                Assertions.assertThat(it.id).isNotNull()
            }.verifyComplete()

        deleteTestData(shoppingCart)
    }

    fun deleteTestData(vararg shoppingCarts: ShoppingCart) {
        shoppingCartRepository.deleteAll(shoppingCarts.toList()).subscribe()
    }
}
