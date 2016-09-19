package com.example.smsm.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MainActivity extends AppCompatActivity {
    private String url = "http://oa.sumavision.com";

    @ViewInject(R.id.button)
    Button button;
    @ViewInject(R.id.txt)
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        com.lidroid.xutils.ViewUtils.inject(this);

//        button = (Button) findViewById(R.id.button);
//        txt = (TextView) findViewById(R.id.txt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a();
            }
        });
    }

    public void a(){
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                txt.setText("\n"+txt.getText()+"onLoading...");
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                txt.setText("\n"+txt.getText()+"OnSuccess\n"+responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                txt.setText("\n"+txt.getText()+"onFailure\n"+s);
            }
        });


    }
}
