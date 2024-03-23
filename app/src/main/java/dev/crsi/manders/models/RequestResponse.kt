package dev.crsi.manders.models

data class RequestResponse(
    val id_request: Int,
    val detail_request: String,
    val status_request: String,
    val dateregister_request: String,
    val dateupdate_request: String,
    val service_id_service: Int,
    val user_id_user: Int
)