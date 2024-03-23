package dev.crsi.manders.models

data class ManderResponse(
    val id_mander: Int,
    val image_mander: String?,
    val ishavecar_mander: Boolean,
    val ishavemoto_mander: Boolean,
    val isactive_mander: Boolean,
    val isvalidate_mander: Boolean,
    val dateregister_mander: String,
    val dateupdate_mander: String,
    val address_mander: String,
    val cc_mander: String,
    val user_id_user: Int
)