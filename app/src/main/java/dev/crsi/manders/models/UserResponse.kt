package dev.crsi.manders.models

data class UserResponse(
    val id_user: Int,
    val name_user: String,
    val lastname_user: String,
    val phone_user: String,
    val dateregister_user: String,
    val dateupdate_user: String,
    val ismander_user: Boolean,
    val image_user: String?,
    val account_id_account: Int,
)