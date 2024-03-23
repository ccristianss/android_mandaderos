package dev.crsi.manders.models

data class ManderResponse(
    val address_mander: String,
    val cc_mander: String,
    val image_mander: Any,
    val isactive_mander: Boolean,
    val ishavecar_mander: Boolean,
    val ishavemoto_mander: Boolean,
    val isvalidate_mander: Boolean,
    val user_id_user: Any
)