package com.coderjj.phonedefend.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coderjj.phonedefend.R;

/**
 * Created by Administrator on 2019/5/9.
 */

public class SettingClickView extends RelativeLayout {

    private TextView tv_titl;
    private TextView tv_des;

    public SettingClickView(Context context) {
        this(context, null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml->view 将设置界面的一个条目转换成view对象
        View.inflate(context, R.layout.setting_click_view, this);
        /*等同于如下
        View view = View.inflate(context, R.layout.setting_item_view, this);
        this.addView(view);*/
        tv_titl = findViewById(R.id.tv_titl);
        tv_des = findViewById(R.id.tv_des);
    }
    public void setTitle(String title){
        tv_titl.setText(title);
    }
    public void setDes(String des){
        tv_des.setText(des);
    }
}
