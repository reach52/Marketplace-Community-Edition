package healthcare.alliedworld.dispergo.medicine.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.reach52.healthcare.util.SecurePreferences
import reach52.marketplace.community.R
import reach52.marketplace.community.databinding.FragmentPaymentWebViewBinding
import reach52.marketplace.community.medicine.view.MedicationPurchaseActivity
import reach52.marketplace.community.medicine.viewmodel.MedicationPurchaseViewModel
import reach52.marketplace.community.util.Constant

class PaymentWebViewFragment : Fragment() {

    lateinit var activity: MedicationPurchaseActivity
    lateinit var binding: FragmentPaymentWebViewBinding
    lateinit var viewModel: MedicationPurchaseViewModel
    lateinit var preferences: SecurePreferences
    lateinit var countDownTimer: CountDownTimer


    companion object{
        var fragment:PaymentWebViewFragment? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity = requireActivity() as MedicationPurchaseActivity
        viewModel = ViewModelProvider(activity)[MedicationPurchaseViewModel::class.java]
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_payment_web_view, container, false)
        fragment = this
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = SecurePreferences(
            this.requireContext(),
            Constant.preferenceName,
            Constant.secureKey,
            true
        )
        viewModel.paymentInvoiceApi()
        viewModel.paymentMutable.observe(viewLifecycleOwner, {
            binding.xenditWebView.settings.javaScriptEnabled = true
            binding.xenditWebView.settings.loadWithOverviewMode = true
            binding.xenditWebView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                }
            }

            Log.d("Xendid", "onViewCreated: webpage fragment")
            binding.xenditWebView.loadUrl(it.invoice_url)
            Log.d("paymentGatewayURL", it.invoice_url)
//            binding.xenditWebView.loadUrl("https://checkout-staging.xendit.co/web/6142bcde651ce324d0507759")

//            countDownStart()
        })

        binding.xenditWebView.settings.javaScriptEnabled = true
        binding.xenditWebView.settings.loadWithOverviewMode = true
        binding.xenditWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }
        }

//        Log.d("Xendid", "onViewCreated: webpage fragment")
////            binding.xenditWebView.loadUrl(it.data.invoice_url)
////        binding.xenditWebView.loadUrl("http://google.com")
//        binding.xenditWebView.loadUrl("https://checkout-staging.xendit.co/web/61430a7a45ad6ce26ae06888")




    }

    private fun countDownStart() {
        countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
                alertDialog.setTitle("Session Expire")
                alertDialog.setCancelable(false)
                alertDialog.setMessage("Payment processing has taken longer than expected. Please check order page for the updated payment status. We will update the status regularly.")
                alertDialog.setPositiveButton(
                    "Ok"
                ) { _, _ ->
                    activity.finish()
                }
                val alert: AlertDialog = alertDialog.create()
                alert.setCanceledOnTouchOutside(false)
                alert.show()
            }
        }.start()
    }

//    fun updateOrderPaymentStatus() {
//        //TODO update payment status and payment mode
//        viewModel.getSingleOrder()
//        viewModel.orderMedicine.observe(viewLifecycleOwner, {
//            if (it.subOrders[0]?.paymentStatus == "PAID") {
//                activity.finish()
//                if (::countDownTimer.isInitialized) {
//                    countDownTimer.cancel()
//                }
//            }
//        })
//    }

    override fun onDetach() {
        super.onDetach()
        fragment = null
    }
}