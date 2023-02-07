package com.example.shoppingcart.domain

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document

@Document("Products")
class Product(
    val name: String,
    val price: Long,
    val quantity: Long,

    @Version
    var version: Long = 0L,

    @Id
    var id: String? = null,
)
