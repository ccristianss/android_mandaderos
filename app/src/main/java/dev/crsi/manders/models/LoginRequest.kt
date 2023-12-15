package dev.crsi.manders.models

data class LoginRequest(
    val email_account: String,
    val password_account: String
)