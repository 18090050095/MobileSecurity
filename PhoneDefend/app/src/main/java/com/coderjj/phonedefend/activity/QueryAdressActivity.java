package com.coderjj.phonedefend.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.coderjj.phonedefend.R;
import com.coderjj.phonedefend.engine.AddressDao;

public class QueryAdressActivity extends AppCompatActivity {

    private TextView mTvResult;
    private Button mBtQuery;
    private EditText mEtPhone;
    private String mNumber;
    private String mAddress;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTvResult.setText(mAddress);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_adress);

        initView();
        initData();
    }

    private void initData() {
        mBtQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumber = mEtPhone.getText().toString();
                if (!TextUtils.isEmpty(mNumber)){
                    queryAdress(mNumber);
                }else {
                    //输入框抖动
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
                    mEtPhone.startAnimation(shake);
                    //自定义插补器
                    /*shake.setInterpolator(new Interpolator() {
                        @Override
                        public float getInterpolation(float input) {
                            return 0;
                        }
                    });*/

                    //手机震动效果
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(2000);
                    //规律震动(震动规则(不震动时间,震动时间,不震动时间,震动时间...),重复次数)
                    vibrator.vibrate(new long[]{2000,5000,2000},-1);
                }
            }
        });

        //5.实时查询
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queryAdress(mEtPhone.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void queryAdress(final String num) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAddress = AddressDao.getAddress(num);
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void initView() {
        mEtPhone = findViewById(R.id.et_phone);
        mBtQuery = findViewById(R.id.bt_query);
        mTvResult = findViewById(R.id.tv_result);
    }
}
