package reach52.marketplace.community.data

import reach52.marketplace.community.medicine.viewmodel.PaymentRequestModel
import reach52.marketplace.community.medicine.viewmodel.PaymentResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentGatewayService {
    @POST("your invoice API")
    fun getPaymentInvoiceUrl(@Body paymentRequestModel: PaymentRequestModel): Call<PaymentResponseModel?>?
}