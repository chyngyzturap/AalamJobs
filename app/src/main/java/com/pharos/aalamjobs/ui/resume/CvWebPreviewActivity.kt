package com.pharos.aalamjobs.ui.resume

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.LayoutInflater
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.local.db.cv.models.ApplicationModel
import com.pharos.aalamjobs.data.network.CvApi
import com.pharos.aalamjobs.data.repository.CvRepository
import com.pharos.aalamjobs.databinding.ActivityCvWebPreviewBinding
import com.pharos.aalamjobs.ui.base.BaseActivity
import com.pharos.aalamjobs.ui.cv.CvViewModel
import com.pharos.aalamjobs.ui.main.MainActivity
import com.pharos.aalamjobs.utils.startNewActivity
import kotlinx.android.synthetic.main.activity_cv_web_preview.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class CvWebPreviewActivity : BaseActivity<CvViewModel, ActivityCvWebPreviewBinding, CvRepository>() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cv_web_preview)

        val html = intent.getIntExtra("cvId", 0)
        val jobIdForApply = intent.getIntExtra("jobIdForApply", 0)
        binding.webView
        WebView.setWebContentsDebuggingEnabled(true)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.setSupportZoom(true)
        binding.webView.loadUrl("http://165.22.88.94:9000/api/resumes/$html/webview/")

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) =
                false

            override fun onPageFinished(view: WebView, url: String) {
                btn_print.setOnClickListener {
                    createWebPrintJob(view)
                }
            }
        }

        binding.btnSendCv.setOnClickListener {
            if (jobIdForApply != 0) {
                val apply = ApplicationModel(jobIdForApply, html)
                viewModel.applyCv(apply)
                startNewActivity(MainActivity::class.java)
            }
        }
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

    override fun getViewModel() = CvViewModel::class.java

    override fun getActivityBinding(inflater: LayoutInflater) = ActivityCvWebPreviewBinding.inflate(layoutInflater)

    override fun getActivityRepository(): CvRepository {
        val token = runBlocking { userPreferences.tokenAccess.first() }
        val apiNoToken = remoteDataSource.buildApiWithoutToken(CvApi::class.java, token)
        val api = remoteDataSource.buildApi(CvApi::class.java, token)

        if (token.isNullOrEmpty()){
            return CvRepository(apiNoToken)
        } else {
            return CvRepository(api)
        }
    }
}
