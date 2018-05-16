package com.example.hegeli.hospitalgudie;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String symptoms = "";
    Handler handler;
    TextView text = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //symptoms = "肩关节脱位";

        Button query = findViewById(R.id.button);
        query.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView symptomsText = findViewById(R.id.symptomsText);
                symptoms = symptomsText.getText().toString();
                text = findViewById(R.id.textView3);
                handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        text.setText(Html.fromHtml((String)msg.obj));
                    }
                };
                new Thread(networkTask).start();
            }
        });
    }

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // 在这里进行 http request.网络请求相关操作
            try {
                URL url = new URL("http://www.hegeli.top/predict?symptoms=" + symptoms + "");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection( );
                conn.setRequestMethod("GET");
                InputStream in = conn.getInputStream();
                byte[] data = new byte[1024];
                in.read(data, 0, 1024);
                String s = new String(data, "UTF-8");
                Message msg = new Message();
                msg.obj = s;
                handler.sendMessage(msg);
                conn.disconnect();
            }catch(Exception e){
                Exception ee = e ;
            }
        }
    };
}
