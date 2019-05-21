package com.coderjj.phonedefend.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.coderjj.phonedefend.R;
import com.coderjj.phonedefend.utils.ConstantValue;
import com.coderjj.phonedefend.utils.SpUtil;

public class SetupOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean setup_over = SpUtil.getBoolean(this, ConstantValue.SETUP_OVER, false);
        if (setup_over) {
            //密码输入成功，导航设置界面完成    ------->停留在设置完成功能列表界面
            setContentView(R.layout.activity_setup_over);
        } else {
            //密码输入成功，导航设置界面没有完成------->跳转到导航界面
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);

            finish();
        }
    }
}
