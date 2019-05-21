package com.coderjj.devicemanager;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private ComponentName mDeviceAdminSample;
    private DevicePolicyManager mDMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt_start = findViewById(R.id.bt_start);
        Button bt_lock = findViewById(R.id.bt_lock);
        Button bt_wipedata = findViewById(R.id.bt_wipedata);
        Button bt_uninstal = findViewById(R.id.bt_uninstal);

        //获取设备管理者对象（上下文环境，广播接收者的字节码文件）
        mDeviceAdminSample = new ComponentName(this, DeviceAdmin.class);
        //锁屏
        mDMP = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "设备管理器");
                startActivity(intent);
            }
        });

        bt_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否激活
                if(mDMP.isAdminActive(mDeviceAdminSample)){
                    mDMP.lockNow();
                    mDMP.resetPassword("123",0);
                }else{
                    //
                }
            }
        });

        bt_wipedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否激活
                if(mDMP.isAdminActive(mDeviceAdminSample)){
                    mDMP.wipeData(0);//手机数据
//                    mDMP.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//sd卡数据
                }else{
                    //
                }
            }
        });

        bt_uninstal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.DELETE");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:"+getPackageName()));
                startActivity(intent);
            }
        });
    }
}
