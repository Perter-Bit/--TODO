package com.example.homework.logic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.example.homework.R;
public class Web extends Activity {
    private TextView btn_return;//返回按钮
    private WebView marketWebView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        btn_return = (TextView) findViewById(R.id.btn_return);
        marketWebView= (WebView) findViewById(R.id.webView);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //支持javascript
        marketWebView.getSettings().setJavaScriptEnabled(true);
        marketWebView.getSettings().setUseWideViewPort(true);
        marketWebView.getSettings().setLoadWithOverviewMode(true);
        //支持页面缩放
        marketWebView.getSettings().setBuiltInZoomControls(true);
        //提升渲染优先级
        //marketWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //不加载网络中的图片资源
        marketWebView.getSettings().setBlockNetworkImage(true);
        //HTML5 Cache
        marketWebView.getSettings().setDomStorageEnabled(true);
        marketWebView.getSettings().setAllowFileAccess(true);
//        marketWebView.getSettings().setAppCacheEnabled(true);

        //优先从本地cache中载入，其次才是从网络中载入，即使内容已经过期
        marketWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        marketWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                //Android TV中可以在这里返回true，按键交由onKeyDown方法处理
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        marketWebView.loadUrl(url);
    }
}
