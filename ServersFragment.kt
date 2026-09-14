package com.seunome.bedrocklauncher.ui

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import com.seunome.bedrocklauncher.R

class ServersFragment : Fragment(R.layout.fragment_servers) {

    private val aternosUrl = "https://aternos.org/go/"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = view.findViewById<WebView>(R.id.webview_aternos)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl(aternosUrl)

        view.findViewById<Button>(R.id.btn_reload).setOnClickListener {
            webView.reload()
        }
    }
}
