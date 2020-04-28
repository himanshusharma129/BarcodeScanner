package com.hs.topscanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class MainActivity : AppCompatActivity() {
    public lateinit var webView : WebView
    val activity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView=findViewById(R.id.webview)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JavaScriptInterface(this), "Android")
        webView.loadUrl("file:///android_asset/index.html");
        val ts = "-barcode-"
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {

                webView.loadUrl("javascript:gotCode('$ts')")
            }
        }

    }

    private fun startQRScanner() {
        IntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {

                val ts: String = result.getContents()
                Toast.makeText(this, ts, Toast.LENGTH_SHORT).show()
                webView.loadUrl("javascript:gotCode('$ts')")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    inner class JavaScriptInterface
    internal constructor(var mContext: Context){

        fun closeMyActivity() {

        }

        @JavascriptInterface
        fun scanBarcode() {
            (activity as MainActivity).startQRScanner()
            }

    }



}
