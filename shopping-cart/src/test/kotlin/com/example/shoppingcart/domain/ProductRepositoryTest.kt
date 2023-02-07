package com.example.shoppingcart.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import reactor.kotlin.test.test

@DataMongoTest
class ProductRepositoryTest @Autowired constructor(
    private val productRepository: ProductRepository,
) {

    @Test
    fun saveProduct() {
        val product = Product("딸기 우유", 1_300, 200)
        val productMono = productRepository.save(product)

        productMono.test()
            .assertNext {
                assertThat(it.id).isNotNull()
            }.verifyComplete()

        deleteTestData(product)
    }

    fun deleteTestData(vararg products: Product) {
        productRepository.deleteAll(products.toList()).subscribe()
    }
}
