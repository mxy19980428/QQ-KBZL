package com.zlqk;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by MXY on 2018/10/6.
 */
public class ZLQK extends AppCompatActivity {

    private Button button, lxzz, tmtx;
    private String cookie, ldw;

    public static final String TAG = "UploadHelper";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private final OkHttpClient client = new OkHttpClient();


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.zlcq);
        cookie = getIntent().getStringExtra("newck");
        String a[] = (getIntent().getStringExtra("ldw")).split("=");
        ldw = a[1];
        Log.e("cookie", cookie);
        Log.e("ldw", ldw);

        button = findViewById(R.id.bottom);
        lxzz = findViewById(R.id.lxzz);
        tmtx = findViewById(R.id.tmtx);

   /*     if (!isWifiProxy()) {
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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    postzz();
                    Thread.sleep(2000);
                    postzz1();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        lxzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=1292008122";//uin是发送过去的qq号码
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception e) {
                    Toast.makeText(ZLQK.this, "我的妈呀！联系失败！QQ:1292008122", Toast.LENGTH_LONG).show();
                }
            }
        });


        tmtx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmtx();
            }
        });
    }


    public void postzz() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("ldw", ldw)
                .add("g", "0")
                .add("bir_new_y", "1899")
                .add("bir_new_m", "1")
                .add("bir_new_d", "1")
                .add("bir_new_lunar", "0")
                .add("bir_new_second", "0")
                .add("jing", "")
                .add("wei", "")
                .add("pos_u", "")
                .add("pos_p", "")
                .add("pos_c", "")
                .add("pos_d", "")
                .add("gx_u", "")
                .add("gx_p", "")
                .add("gx_c", "")
                .add("gx_d", "")
                .add("detail_addr", "")
                .add("xx", "0")
                .add("realname", "")
                .add("english_name", "")
                .add("mobl", "")
                .add("tel", "")
                .add("mail", "")
                .add("lg1", "")
                .add("lg2", "")
                .add("lg3", "")
                .add("schl", "")
                .add("hmpg", "")
                .add("commt", "")
                .add("work", "").build();
        post(okHttpClient, formBody);
    }


    public void postzz1() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("ldw", ldw)
                .add("sx", "0")
                .add("xz", "0").build();
        post(okHttpClient, formBody);
    }

    public void tmtx() {
        /*Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getResources().getResourcePackageName(R.drawable.qq));
        Log.e("url", "url=" + String.valueOf(uri));

        String imageType = "multipart/form-data";
        File file = new File("http://q1.qlogo.cn/g?b=qq&s=100&nk=1292008122");// imgUrl为图片位置
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/gif"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "head_image", fileBody)
                .addFormDataPart("imagetype", imageType)
                .build();
        Request request = new Request.Builder().url("http://face.qq.com/client/uploadflash.php").post(requestBody).header("Cookie", cookie).build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                Log.e("html", html);
            }
        });*/
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(getRequest("http://face.qq.com/client/uploadflash.php", Environment.getExternalStorageDirectory() + "/短视频解析/test/test.gif", cookie));
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("mxy", "上传成功=" + response.body().string().toString());
            }
        });


    }

    private Request getRequest(String url, String fileNames, String cookie) {
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(getRequestBody(fileNames)).header("Cookie", cookie).header("Content-Type", "multipart/form-data; boundary=---");
        return builder.build();
    }


    private RequestBody getRequestBody(String fileNames) {
        //创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();

        File file = new File(fileNames); //生成文件
        //根据文件的后缀名，获得文件类型
        String fileType = getMimeType(file.getName());
        builder.addFormDataPart( //给Builder添加上传的文件
                "gif", file.getName(), //文件的文字，服务器端用来解析的
                RequestBody.create(MediaType.parse(fileType), file) //创建RequestBody，把上传的文件放入
        );
        return builder.build(); //根据Builder创建请求
    }

    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,所有的可执行程序
        }
        return contentType;
    }


    private void post(OkHttpClient okHttpClient, FormBody formBody) throws IOException {
        Request request = new Request.Builder().url("http://id.qq.com/cgi-bin/userinfo_mod").header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36").header("Referer", "http://id.qq.com/myself/myself.html?ver=10045&").header("Cookie", cookie).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string().toString();
                Log.e("html:", "html=" + html);
                try {
                    JSONObject jsonObject = new JSONObject(html);
                    int code = jsonObject.getInt("ec");
                    int msg = jsonObject.getInt("mf");
                    Message message = new Message();
                    message.obj = msg;
                    message.what = code;
                    mHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
                Toast.makeText(ZLQK.this, "清空完成，请前往QQ查看", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ZLQK.this, msg.obj + "", Toast.LENGTH_SHORT).show();
            }

        }
    };

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


}
