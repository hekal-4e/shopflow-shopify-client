package com.shopflow.app.domain.model

data class Address(
    val id: String,
    val address1: String,
    val address2: String? = null,
    val city: String,
    val province: String? = null,
    val country: String,
    val zip: String
)

data class Customer(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String? = null,
    val defaultAddress: Address? = null,
    val addresses: List<Address> = emptyList(),
    val accessToken: String = "",
    val tokenExpiry: Long = 0L
)
