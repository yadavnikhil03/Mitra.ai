package com.mitra.app.ui.chat

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class L2DView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : WebView(context, attrs) {

    interface Listener {
        fun onFaceReady()
        fun onFaceError(message: String)
    }

    var listener: Listener? = null
    private var faceReady = false

    init {
        setBackgroundColor(Color.TRANSPARENT)
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
            }

            @SuppressLint("WebViewClientOnReceivedError")
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                if (request.isForMainFrame) {
                    listener?.onFaceError(error.description?.toString() ?: "face load failed")
                }
            }
        }
        addJavascriptInterface(Bridge(), "MitraAndroid")
    }

    fun load() {
        loadUrl("file:///android_asset/live2d/index.html")
    }

    fun setMouth(open: Float) {
        if (!faceReady) return
        evaluateJavascript("window.MitraFace && window.MitraFace.setMouth($open)", null)
    }

    fun setState(state: String) {
        if (!faceReady) return
        evaluateJavascript("window.MitraFace && window.MitraFace.setState('$state')", null)
    }

    fun refreshSize() {
        if (!faceReady) return
        evaluateJavascript("window.dispatchEvent(new Event('resize'))", null)
    }

    fun lookAt(x: Float, y: Float) {
        if (!faceReady) return
        evaluateJavascript("window.MitraFace && window.MitraFace.lookAt($x, $y)", null)
    }

    private inner class Bridge {
        @JavascriptInterface
        fun onFaceReady() {
            faceReady = true
            listener?.onFaceReady()
        }

        @JavascriptInterface
        fun onFaceError(message: String) {
            listener?.onFaceError(message)
        }
    }
}