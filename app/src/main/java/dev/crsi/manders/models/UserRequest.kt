package dev.crsi.manders.models

data class UserRequest(
    val account_id_account: Int,
    val name_user: String,
    val lastname_user: String,
    val phone_user: String
)