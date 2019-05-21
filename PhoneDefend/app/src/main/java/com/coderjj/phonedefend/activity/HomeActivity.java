package com.coderjj.phonedefend.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coderjj.phonedefend.R;
import com.coderjj.phonedefend.utils.ConstantValue;
import com.coderjj.phonedefend.utils.Md5Util;
import com.coderjj.phonedefend.utils.SpUtil;

/**
 * Created by Administrator on 2019/5/6.
 */

public class HomeActivity extends Activity {

    private GridView gv_home;
    private int[] mDrawableIds;
    private String[] mTitleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initData();
    }

    private void initView() {
        gv_home = findViewById(R.id.gv_home);
    }

    private void initData() {
        //准备数据（文字，图片）
        mTitleStr = new String[]{
                "手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计"
                , "手机杀毒", "缓存清理", "高级工具", "设置中心"
        };
        mDrawableIds = new int[]{
                R.drawable.home_safe, R.drawable.home_callmasgsafe,
                R.drawable.home_apps, R.drawable.home_taskmanager,
                R.drawable.home_netmanager, R.drawable.home_trojan,
                R.drawable.home_sysoptimize, R.drawable.home_tools,
                R.drawable.home_settings
        };
        //九宫格控件设置数据适配器
        gv_home.setAdapter(new MyAdapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //开启对话框
                        showDialog();
                        break;
                    case 1:
                        //跳转到通信卫士模块
                        startActivity(new Intent(getApplicationContext(),BlackNumberActivity.class));
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        Intent aToolIntent = new Intent(getApplicationContext(), AtoolActivity.class);
                        startActivity(aToolIntent);
                        break;
                    case 8:
                        Intent settingIntent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(settingIntent);
                        break;
                }
            }
        });
    }

    private void showDialog() {
        //判断本地是否有存储密码(sp 字符串)
        String psd = SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");
        if (TextUtils.isEmpty(psd)) {
            //1.初始设置密码对话框
            showSetPsdDialog();
        } else {
            //2.确认密码对话框
            showConfirmPsdDialog();
        }


    }

    private void showConfirmPsdDialog() {
        //因为需要自定义对话框的展示样式，所以需要调用dialog.setView(view);
        //view是由自己编写的xml转换成view对象xml---->view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        final View view = View.inflate(this, R.layout.dialog_confirm_psd, null);
        //        dialog.setView(view);
        dialog.setView(view,0,0,0,0);
        dialog.show();

        Button bt_submit = view.findViewById(R.id.bt_submit);
        final Button bt_cancel = view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_confirm_psd = view.findViewById(R.id.et_confirm_psd);

                String confirmPsd = et_confirm_psd.getText().toString();

                if (!TextUtils.isEmpty(confirmPsd)){
                    String psd = SpUtil.getString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, "");
                    if (Md5Util.encoder(confirmPsd).equals(psd)){
                        //进入手机防盗模块
                        Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
                        startActivity(intent);
                        //跳转到新的界面以后需要关闭对话框
                        dialog.dismiss();
                    }else{
                        Toast.makeText(getApplicationContext(),"确认密码错误",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"请输入密码",Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showSetPsdDialog() {
        //因为需要自定义对话框的展示样式，所以需要调用dialog.setView(view);
        //view是由自己编写的xml转换成view对象xml---->view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();

        final View view = View.inflate(this, R.layout.dialog_set_psd, null);
//        dialog.setView(view);
        dialog.setView(view,0,0,0,0);
        dialog.show();

        Button bt_submit = view.findViewById(R.id.bt_submit);
        final Button bt_cancel = view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_set_psd = view.findViewById(R.id.et_set_psd);
                EditText et_confirm_psd = view.findViewById(R.id.et_confirm_psd);

                String psd = et_set_psd.getText().toString();
                String confirmPsd = et_confirm_psd.getText().toString();

                if (!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(confirmPsd)){
                    if (psd.equals(confirmPsd)){
                        //进入手机防盗模块
                        Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
                        startActivity(intent);
                        //跳转到新的界面以后需要关闭对话框
                        dialog.dismiss();

                        SpUtil.putString(getApplicationContext(),
                                ConstantValue.MOBILE_SAFE_PSD, Md5Util.encoder(confirmPsd));
                    }else{
                        Toast.makeText(getApplicationContext(),"确认密码错误",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"请输入密码",Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTitleStr.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            TextView tv_title = view.findViewById(R.id.tv_titl);
            ImageView iv_icon = view.findViewById(R.id.iv_icon);

            tv_title.setText(mTitleStr[position]);
            iv_icon.setImageResource(mDrawableIds[position]);
            return view;
        }
    }
}
