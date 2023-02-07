package com.example.shoppingcart.domain

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ShoppingCartRepository : ReactiveMongoRepository<ShoppingCart, String>
