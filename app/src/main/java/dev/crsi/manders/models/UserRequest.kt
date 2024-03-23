package dev.crsi.manders.models

data class UserRequest(
    val account_id_account: Int,
    val name_user: String,
    val lastname_user: String,
    val phone_user: String
) {
    constructor(account_id_account: Int, name_user: String, lastname_user: String) : this(account_id_account, name_user, lastname_user, "")
}