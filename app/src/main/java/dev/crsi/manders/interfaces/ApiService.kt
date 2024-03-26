package dev.crsi.manders.interfaces

import dev.crsi.manders.models.AccountPasswordChangeRequest
import dev.crsi.manders.models.AccountRequest
import dev.crsi.manders.models.AccountResponse
import dev.crsi.manders.models.DetailRequestResponse
import dev.crsi.manders.models.LoginRequest
import dev.crsi.manders.models.LoginResponse
import dev.crsi.manders.models.ManderRequest
import dev.crsi.manders.models.ManderActiveRequest
import dev.crsi.manders.models.ManderResponse
import dev.crsi.manders.models.ServiceResponse
import dev.crsi.manders.models.UserRequest
import dev.crsi.manders.models.UserResponse
import dev.crsi.manders.ui.UpdateProfileManderActivity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("login/")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("account/")
    fun createAccount(@Body request: AccountRequest): Call<AccountResponse>

    @PUT("account/{id}/")
    fun changePasswordAccount(
        @Path("id") id: Int,
        @Body request: AccountPasswordChangeRequest
    ): Call<AccountResponse>

    @GET("user/")
    fun getUsers(): Call<List<UserResponse>>

    @GET("user/{id}")
    fun getUserById(@Path("id") id: Int): Call<UserResponse>

    @POST("user/")
    fun createUser(@Body request: UserRequest): Call<UserResponse>


    //si es un listado total la respuesta o Callbal debe ser de tipo List o Array O MutabeList
    @GET("request/")
    fun getRequest(): Call<List<DetailRequestResponse>>

    @GET("service/")
    fun getService(): Call<List<ServiceResponse>>

    @PUT("user/{id}/")
    fun updateUser(@Path("id") id: Int, @Body userRequest: UserRequest): Call<UserResponse>

    //lista de mandaderos

    @GET("mander/")
    fun getmander(): Call<List<ManderResponse>>

    @PUT("mander/{id}/")
    fun updateMander(@Path("id") id: Int, @Body manderRequest: ManderRequest): Call<ManderResponse>



    @GET("mander/")
    fun getManders(): Call<List<ManderResponse>>




    @PATCH("mander/{id}/")
    fun updateActiveMander(
        @Path("id") id: Int,
        @Body manderActiveRequest: ManderActiveRequest
    ): Call<ManderResponse>
}