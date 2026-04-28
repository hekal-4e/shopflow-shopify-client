package com.shopflow.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CartWebhookRequest(
    @SerializedName("user_email")
    val userEmail: String,
    @SerializedName("product_name")
    val productName: String
)