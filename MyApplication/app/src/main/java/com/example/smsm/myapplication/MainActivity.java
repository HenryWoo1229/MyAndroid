package com.example.smsm.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private String url = "http://oa.sumavision.com";
    HttpHandler handler;

    @ViewInject(R.id.button)
    Button button;
    @ViewInject(R.id.download)
    Button bn_download;
    @ViewInject(R.id.cancel)
    Button bn_cancel;
    @ViewInject(R.id.txt)
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        com.lidroid.xutils.ViewUtils.inject(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpGet();
            }
        });

        bn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpDownload();
                Toast.makeText(MainActivity.this, "downloading", Toast.LENGTH_SHORT).show();
            }
        });

        bn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.cancel();  //stop download
                Toast.makeText(MainActivity.this, "download cancel", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void httpGet() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                txt.setText("onLoading...");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                txt.setText("\n" + txt.getText() + "OnSuccess\n" + responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                txt.setText("\n" + txt.getText() + "onFailure\n" + s);
            }
        });


    }

    public void httpDownload() {

        String downUrl = "http://192.166.66.22/download/suma_upg.img";
        String target = "/sdcard/suma_upg.img";
        HttpUtils http = new HttpUtils();
        handler = http.download(downUrl, target,
                true,   //如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将重新下载。
                true,   //如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                new RequestCallBack<File>() {

                    @Override
                    public void onStart() {
                        txt.setText("onStart\n");
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        txt.setText("onLoading:" + current + "/" + total + "\n");

                    }

                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        txt.setText("onSuccess " + responseInfo.result + "\n");
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        txt.setText("ExceptionCode:" + e.getExceptionCode() + ", msg:" + s + "\n");
                    }
                });


    }
}
