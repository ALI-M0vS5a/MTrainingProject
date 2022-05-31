package com.example.fetchgate.network

data class User(
      val access_token: String?,
//    val created_at: String,
//    val email: String,
//    val email_verified_at: Any,
//    val id: Int,
//    val name: String,
//    val updated_at: String
      val username: String,
      val password: String,
      val email: String
)
