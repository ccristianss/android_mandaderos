package dev.crsi.manders.interfaces

import dev.crsi.manders.models.AccountRequest
import dev.crsi.manders.models.AccountResponse
import dev.crsi.manders.models.LoginRequest
import dev.crsi.manders.models.LoginResponse
import dev.crsi.manders.models.UserRequest
import dev.crsi.manders.models.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("login/")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("account/")
    fun createAccount(@Body request: AccountRequest): Call<AccountResponse>

    @GET("user/{id}")
    fun getUserById(@Path("id") id: Int): Call<UserResponse>

    @POST("user/")
    fun createUser(@Body request: UserRequest): Call<UserResponse>

}