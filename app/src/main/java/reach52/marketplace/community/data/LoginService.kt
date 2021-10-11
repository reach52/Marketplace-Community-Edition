package reach52.marketplace.community.data


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("/api/user/login")
    fun loginUser(@Body loginRequestModel: LoginRequestModel): Call<LoginResponseModel>



}