package reach52.marketplace.community.data


import reach52.marketplace.community.medicine.viewmodel.PaymentRequestModel
import reach52.marketplace.community.medicine.viewmodel.PaymentResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("your login API")
    fun loginUser(@Body loginRequestModel: LoginRequestModel): Call<LoginResponseModel>

    @POST("your payment API")
        fun getPaymentInvoiceUrl(@Body paymentRequestModel: PaymentRequestModel): Call<PaymentResponseModel?>?

}