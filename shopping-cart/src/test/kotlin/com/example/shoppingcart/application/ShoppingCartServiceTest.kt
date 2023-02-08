package com.example.shoppingcart.application

import com.example.shoppingcart.application.dto.ShoppingCartRequest
import com.example.shoppingcart.domain.Product
import com.example.shoppingcart.domain.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.kotlin.test.test
import reactor.kotlin.test.verifyError

@SpringBootTest
class ShoppingCartServiceTest @Autowired constructor(
    private val shoppingCartService: ShoppingCartService,
    private val productRepository: ProductRepository,
) {
    @Test
    fun `장바구니에 상품을 등록한다`() {
        val product = Product("딸기 우유", 1_300, 200)
        productRepository.save(product).block()

        val request = ShoppingCartRequest(mapOf("딸기 우유" to 10))
        shoppingCartService.register(request)
            .test()
            .assertNext { assertThat(it.id).isNotNull() }
            .verifyComplete()
    }

    @Test
    fun `존재하지 않는 상품을 장바구니에 등록할시 예외가 발생한다`() {
        val request = ShoppingCartRequest(mapOf("존재하지 않는 상품" to 10))
        shoppingCartService.register(request)
            .test()
            .verifyError<IllegalArgumentException>()
    }
}
