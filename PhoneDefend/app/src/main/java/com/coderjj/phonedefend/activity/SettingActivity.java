package com.coderjj.phonedefend.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.coderjj.phonedefend.R;
import com.coderjj.phonedefend.service.AddressService;
import com.coderjj.phonedefend.service.BlackNumberService;
import com.coderjj.phonedefend.utils.ConstantValue;
import com.coderjj.phonedefend.utils.ServiceUtil;
import com.coderjj.phonedefend.utils.SpUtil;
import com.coderjj.phonedefend.view.SettingClickView;
import com.coderjj.phonedefend.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {


    private String[] mToastStyleDes;
    private int mToastStyle;
    private SettingClickView scv_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
        initAddress();
        initToastSet();
        initToastLocation();
        initBlackNumber();
    }

    private void initBlackNumber() {
        final SettingItemView siv_blacknumber = findViewById(R.id.siv_blacknumber);
        boolean isCheckAddress = ServiceUtil.isRunning(getApplicationContext(), "com.coderjj.phonedefend.service.BlackNumberService");
        siv_blacknumber.setCheck(isCheckAddress);
        siv_blacknumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_blacknumber.isCheck();
                siv_blacknumber.setCheck(!isCheck);
                if (!isCheck) {
                    startService(new Intent(getApplicationContext(), BlackNumberService.class));
                } else {
                    stopService(new Intent(getApplicationContext(), BlackNumberService.class));
                }
            }
        });
    }

    private void initToastLocation() {
        SettingClickView scv_toast_location = findViewById(R.id.scv_toast_location);
        scv_toast_location.setTitle("归属地提示框的位置");
        scv_toast_location.setDes("设置归属地提示框的位置");
        scv_toast_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ToastLocationActivity.class));
            }
        });
    }

    private void initToastSet() {
        scv_address = findViewById(R.id.scv_address);
        scv_address.setTitle("设置归属地显示风格");
        mToastStyleDes = new String[]{"透明", "橙色", "蓝色", "灰色", "绿色"};
        mToastStyle = SpUtil.getInt(this, ConstantValue.TOAST_STYLE_INDEX, 0);
        scv_address.setDes(mToastStyleDes[mToastStyle]);
        //单击点击事件，弹出对话框
        scv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastDialog();
            }
        });
    }

    /**
     * 创建选中显示样式的对话框
     */
    private void showToastDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.home_apps);
        builder.setTitle("请选择归属地样式");

        /*
        * 选择单个选择条目事件监听参数定义
        * 1.string类型的数组描述颜色文字数组
        * 2.弹出对话框的时候的选中条目索引
        * 3.点击某一个条目后触发的点击事件
        */
        builder.setSingleChoiceItems(mToastStyleDes, mToastStyle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SpUtil.putInt(getApplication(),ConstantValue.TOAST_STYLE_INDEX,which);
                dialog.dismiss();
                scv_address.setDes(mToastStyleDes[which]);
                mToastStyle = which;
            }
        });
        //消极按钮处理
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 是否显示电话号码归属地的方法
     */
    private void initAddress() {
        final SettingItemView siv_address = findViewById(R.id.siv_address);
        boolean isCheckAddress = ServiceUtil.isRunning(getApplicationContext(), "com.coderjj.phonedefend.service.AddressService");
        siv_address.setCheck(isCheckAddress);
        siv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_address.isCheck();
                siv_address.setCheck(!isCheck);
                if (!isCheck) {
                    //6.0以上申请弹窗权限
                    //开启服务，管理吐司
                    startService(new Intent(getApplicationContext(), AddressService.class));
                } else {
                    stopService(new Intent(getApplicationContext(), AddressService.class));
                }
            }
        });
    }

    private void initUpdate() {
        final SettingItemView siv_update = findViewById(R.id.siv_update);

        //获取已有的开关状态用于显示
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*checkbox点击事件冲突问题解决思路：
                * 1.设置CheckBox不响应，事件便会回传给父view
                * 2.父view对事件拦截，阻止传递到CheckBox*/
                boolean isCheck = siv_update.isCheck();
                siv_update.setCheck(!isCheck);
                //将取反后的状态存入SP
                SpUtil.putBoolean(getApplication(), ConstantValue.OPEN_UPDATE, !isCheck);
            }
        });
    }

}
