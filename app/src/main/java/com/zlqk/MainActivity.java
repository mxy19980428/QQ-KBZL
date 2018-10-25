package com.zlqk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private WebView web;
    private String ck = "";
    private OkHttpClient okHttpClient = new OkHttpClient();
    private String sessionID;
    private String newck;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        web = findViewById(R.id.web);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setBuiltInZoomControls(false);


/*        if (!isWifiProxy()) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请关闭代理再使用本软件！")
                    .setCancelable(false)
                    .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    }).create().show();
            return;
        }*/


        //不使用缓存：
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


        web.loadUrl("https://xui.ptlogin2.qq.com/cgi-bin/xlogin?appid=1006102&daid=1&style=23&hide_border=1&proxy_url=http%3A%2F%2Fid.qq.com%2Flogin%2Fproxy.html&s_url=http://id.qq.com/index.html");
        web.setWebViewClient(new MyWebViewClient());

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ck = MyWebViewClient.getCookie();
                Log.e("ck", "ck = " + ck);
                if (ck.length() > 50) {
                    initCode();
                }

                if (sessionID != null) {
                    handler.removeCallbacks(this);
                    Intent intent = new Intent(MainActivity.this, ZLQK.class);
                    intent.putExtra("newck", newck);
                    intent.putExtra("ldw", sessionID);
                    startActivity(intent);
                    finish();
                } else {
                    handler.postDelayed(this, 1000);
                }

            }
        };
        handler.postDelayed(runnable, 1000);
    }


    /**
     * 初始化验证码
     */
    public void initCode() {
        Request request = new Request.Builder().url("http://id.qq.com/cgi-bin/get_base_key").header("Content-Type", "application/x-www-form-urlencoded").header("Referer", "http://id.qq.com/index.html").header("Cookie", ck).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//        showToast("验证码加载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取session的操作，session放在cookie头，且取出后含有“；”，取出后为下面的 s （也就是jsesseionid）
                Headers headers = response.headers();
                Log.e("info_headers", "header " + headers);
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                Log.e("info_cookies", "onResponse-size: " + cookies);
                sessionID = session.substring(0, session.indexOf(";"));
                Log.e("info_s", "session is :" + sessionID);
                newck = ck + "; " + sessionID;
                Log.e("newck", "newck = " + newck);
            }
        });
    }


    public boolean isWifiProxy() {
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(this);
            proxyPort = android.net.Proxy.getPort(this);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.deleteDatabase("WebView.db");
        this.deleteDatabase("WebViewCache.db");
        web.clearCache(true);
        web.clearFormData();
        getCacheDir().delete();


        //清空所有Cookie
        CookieSyncManager.createInstance(this);  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

        web.setWebChromeClient(null);
        web.setWebViewClient(null);
        web.getSettings().setJavaScriptEnabled(false);
        web.clearCache(true);
    }
}