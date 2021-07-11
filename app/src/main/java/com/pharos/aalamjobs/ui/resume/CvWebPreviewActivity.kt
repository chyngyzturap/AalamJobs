package com.pharos.aalamjobs.ui.resume

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.prefs.UserPreferences
import kotlinx.android.synthetic.main.activity_cv_web_preview.*

class CvWebPreviewActivity : AppCompatActivity() {

    protected lateinit var userPreferences: UserPreferences

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cv_web_preview)

        val html = intent.getIntExtra("cvId", 0)


        val bearer =
            "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjMyOTk1MDcyLCJqdGkiOiJlYmMxYjRjZDhjMGI0ZDgwYjQ0NTFjOTg4NTNjMmM1YSIsInVzZXJfaWQiOjF9._-aPFkdemIJ874NJHAhZ4j4S-NyR1pjsH2VdnilP6T8"
//val chik = "Chika"
        val headerMap = HashMap<String, String>()
        headerMap["Authorization"] = bearer
//        headerMap["Chika"] = chik

        WebView.setWebContentsDebuggingEnabled(true)
//        web_view.settings.useWideViewPort = true
//        web_view.settings.loadWithOverviewMode = true
        web_view.settings.javaScriptEnabled = true
        web_view.settings.builtInZoomControls = true
//        web_view.settings.databaseEnabled = true
        web_view.settings.domStorageEnabled = true
        web_view.settings.setSupportZoom(true)
        Log.e("ololo", headerMap.toString())
        web_view.loadUrl("http://165.227.143.167:9000/api/resumes/$html/webview/")



        web_view.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) =
                false

            override fun onPageFinished(view: WebView, url: String) {
                btn_print.setOnClickListener {
                    createWebPrintJob(view)
                }
            }
        }

//        if (html != 0) {
//            web_view.settings.javaScriptEnabled = true
//            web_view.settings.builtInZoomControls = true
//            web_view.settings.setSupportZoom(true)
//            web_view.loadUrl("http://aalamjobs.com")
//
//            web_view.webViewClient = object : WebViewClient() {
//
//                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) =
//                    false
//                override fun onPageFinished(view: WebView, url: String) {
//                    btn_print.setOnClickListener {
//                        createWebPrintJob(view)
//                    }
//                    }
//                }
//            }
    }

    private fun createWebPrintJob(webView: WebView) {

        // Get a PrintManager instance
        (getSystemService(Context.PRINT_SERVICE) as? PrintManager)?.let { printManager ->

            val jobName = "${getString(R.string.app_name)} CV"

            // Get a print adapter instance
            val printAdapter = webView.createPrintDocumentAdapter(jobName)


            // Create a print job with name and adapter instance
            printManager.print(
                jobName,
                printAdapter,
                PrintAttributes.Builder().setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .build()
            ).also { printJob ->
                Log.d("ololo", "createWebPrintJob: $printJob")
            }
        }
    }
}
