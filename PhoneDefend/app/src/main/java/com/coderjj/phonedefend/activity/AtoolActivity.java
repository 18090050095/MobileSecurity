package com.coderjj.phonedefend.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.coderjj.phonedefend.R;
import com.coderjj.phonedefend.engine.SmsBackUp;

import java.io.File;

public class AtoolActivity extends AppCompatActivity {

    private TextView mTvQueryPhoneAdress;
    private TextView mTvSmsBackUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);

        //归属地查询
        initPhoneAddress();
        //短信备份
        initSMSBackUp();
        //常用号码查询
        initCommonNumberQuery();
    }

    private void initCommonNumberQuery() {
        findViewById(R.id.tv_commonnumber_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AtoolActivity.this,CommonNumberQueryActivity.class));
            }
        });
    }

    private void initPhoneAddress() {
        mTvQueryPhoneAdress = findViewById(R.id.query_phone_adress);
        mTvQueryPhoneAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QueryAdressActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initSMSBackUp() {
        mTvSmsBackUp = findViewById(R.id.sms_backup);
        mTvSmsBackUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
            }
        });
    }

    private void showProgressDialog() {
        //1.创建一个进度条对话框
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.drawable.ic_next_normal);
        progressDialog.setTitle("短信备份");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

        //2.调用备份短信方法
        new Thread(){
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"sms.xml";
                SmsBackUp.backUp(getApplicationContext(),path, new SmsBackUp.CallBack() {
                    @Override
                    public void setMax(int max) {
                        progressDialog.setMax(max);
                    }

                    @Override
                    public void updateProgress(int progress) {
                        progressDialog.setProgress(progress);
                    }
                });
                progressDialog.dismiss();
            }
        }.start();
    }
}
