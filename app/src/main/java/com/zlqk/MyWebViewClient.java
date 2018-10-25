package com.zlqk;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Legend
 * @data by on 2018/4/6.
 * @description
 */

public class MyWebViewClient extends WebViewClient {

    private static String skey;
    private static String cookie = "";

    @Override
    public boolean shouldOverrideUrlLoading(WebView webview, String url) {
        webview.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookie = cookieManager.getCookie("http://id.qq.com/index.html");
        Log.e("Cookies", "Cookies = " + cookie);
        super.onPageFinished(view, url);
    }


    public static String getCookie() {
        return cookie;
    }

}