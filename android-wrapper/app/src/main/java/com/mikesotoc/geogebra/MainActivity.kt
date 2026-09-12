package com.mikesotoc.geogebra

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.FrameLayout
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : Activity() {
    private lateinit var root: FrameLayout
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // GeoGebra contains its own mathematical keyboard. Keep the Android window
        // resizable as the IME / desktop taskbar changes the usable area.
        @Suppress("DEPRECATION")
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window.navigationBarColor = Color.BLACK

        // Do not draw the WebView underneath desktop/system bars. The previous
        // edge-to-edge implementation padded WebView itself; WebView then kept a
        // larger CSS layout viewport and GeoGebra's fixed bottom keyboard could be
        // clipped. A padded native parent makes WebView's measured size equal the
        // real usable window instead.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        root = FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.WHITE)
        }

        webView = WebView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.WHITE)
        }
        root.addView(webView)
        setContentView(root)

        root.setOnApplyWindowInsetsListener { view, insets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val system = insets.getInsets(
                    WindowInsets.Type.systemBars() or
                        WindowInsets.Type.displayCutout()
                )
                // IME may be taller than the navigation/task bar. max() prevents
                // adding both heights while still keeping the whole keyboard visible.
                val ime = insets.getInsets(WindowInsets.Type.ime())
                view.setPadding(
                    maxOf(system.left, ime.left),
                    system.top,
                    maxOf(system.right, ime.right),
                    maxOf(system.bottom, ime.bottom)
                )
            } else {
                @Suppress("DEPRECATION")
                view.setPadding(
                    insets.systemWindowInsetLeft,
                    insets.systemWindowInsetTop,
                    insets.systemWindowInsetRight,
                    insets.systemWindowInsetBottom
                )
            }

            // Insets are handled by the parent. Do not also pad WebView's page.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowInsets.Builder(insets)
                    .setInsets(WindowInsets.Type.systemBars(), android.graphics.Insets.NONE)
                    .setInsets(WindowInsets.Type.displayCutout(), android.graphics.Insets.NONE)
                    .build()
            } else {
                @Suppress("DEPRECATION")
                insets.replaceSystemWindowInsets(0, 0, 0, 0)
            }
        }
        root.requestApplyInsets()

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true

        webView.loadUrl("https://www.geogebra.org/classic")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            root.requestApplyInsets()
            // GeoGebra uses viewport-dependent fixed panels. Force a fresh layout
            // after maximize/fullscreen/freeform transitions in Android desktop mode.
            webView.postDelayed({
                webView.evaluateJavascript(
                    "window.dispatchEvent(new Event('resize'));", null
                )
            }, 120)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }
}
