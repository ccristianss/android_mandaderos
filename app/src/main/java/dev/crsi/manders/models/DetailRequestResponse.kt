package dev.crsi.manders.models

data class DetailRequestResponse(
    val dateregister_request: String,
    val dateupdate_request: String,
    val detail_request: String,
    val id_request: Int,
    val service_id_service: Int,
    val status_request: String,
    val user_id_user: Int
)