package dev.crsi.manders.models

data class AccountResponse(
    val id_account: Int,
    val email_account: String,
    val password_account: String,
    val dateregister_account: String,
    val dateupdate_account: String,
    val isadmin_account: Boolean
)