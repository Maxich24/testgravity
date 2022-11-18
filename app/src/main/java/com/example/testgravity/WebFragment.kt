package com.example.testgravity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment


class WebFragment : Fragment() {
    private var webviewstate: Bundle? = null
    private var sWebView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onPause() {
        super.onPause()
        webviewstate = Bundle()
        sWebView?.saveState(webviewstate!!)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_web, container, false)
        sWebView = rootView.findViewById(R.id.sWebView)

        sWebView!!.canGoBack()
        sWebView!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action === MotionEvent.ACTION_UP && sWebView!!.canGoBack()) {
                sWebView!!.goBack()
                return@OnKeyListener true
            }
            false
        })

        sWebView!!.settings.javaScriptEnabled = true
        sWebView!!.webViewClient = WebViewClient()

        if(webviewstate==null){
            val activity: MainActivity? = activity as MainActivity?
            val url: String? = activity?.getUrl()
            if (url != null) {
                Log.d("MyLog url", url)
                sWebView!!.loadUrl(url)
            }
        }else{
            sWebView?.restoreState(webviewstate!!)
        }
        return rootView
    }


}