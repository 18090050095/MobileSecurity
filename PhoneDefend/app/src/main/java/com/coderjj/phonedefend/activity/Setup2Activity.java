package com.coderjj.phonedefend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.coderjj.phonedefend.R;
import com.coderjj.phonedefend.utils.ConstantValue;
import com.coderjj.phonedefend.utils.SpUtil;
import com.coderjj.phonedefend.view.SettingItemView;

public class Setup2Activity extends BaseStepActivity {

    private String SIMNuber;
    private SettingItemView mSimBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        initView();
        initData();
    }

    private void initData() {
        SIMNuber = SpUtil.getString(getApplication(), ConstantValue.SIM, "");
        if (TextUtils.isEmpty(SIMNuber)) {
            mSimBound.setCheck(false);
        } else {
            mSimBound.setCheck(true);
        }
    }

    private void initView() {
        mSimBound = findViewById(R.id.sim_bound);

        mSimBound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSimBound.isCheck()) {
                    setSimInfo(false);
                    mSimBound.setCheck(false);
                } else {
                    setSimInfo(true);
                    mSimBound.setCheck(true);
                }
            }
        });
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNext();
            }
        });
        findViewById(R.id.bt_pre).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPre();
            }
        });

    }

    @Override
    public void goToPre() {
        Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void goToNext() {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * 设置配置文件中的sim信息
     */
    public void setSimInfo(boolean isSet) {
        // 通过ture和false控制是否把sim序列号储存到配置信息中
        if (isSet) {
            // 如果是true拿到电话管家
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            // 获得sim卡序列号
            String number = tm.getSimSerialNumber();
            // 存入配置文件中
            SpUtil.putString(getApplication(), ConstantValue.SIM, number);
        } else {
            // 如果为false就是不存储序列号
            // 先判断配置文件中的序列号是否为空
            // 为空则不做操作
            if (!TextUtils.isEmpty(SIMNuber)) {
                // 不为空这将SIM卡信息清空
                SpUtil.putString(getApplication(), ConstantValue.SIM, "");
            }
        }
    }
}
