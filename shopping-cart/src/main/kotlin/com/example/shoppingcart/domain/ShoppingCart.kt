package com.example.shoppingcart.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("ShoppingCarts")
class ShoppingCart(
    val items: Map<String, Int>,

    @Id
    var id: String? = null,
)
