package com.coderjj.phonedefend.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coderjj.phonedefend.R;

/**
 * Created by Administrator on 2019/5/9.
 */

public class SettingItemView extends RelativeLayout {

    private CheckBox cb_box;
    private TextView tv_des;
    public static final String NAME_SPACE = "http://schemas.android.com/apk/res/com.coderjj.phonedefend";
    public static final String TAG = "SettingItemView";
    private String mDestitle;
    private String mDesoff;
    private String mDeson;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml->view 将设置界面的一个条目转换成view对象
        View.inflate(context, R.layout.setting_item_view, this);
        /*等同于如下
        View view = View.inflate(context, R.layout.setting_item_view, null);
        this.addView(view);*/
        TextView tv_title = findViewById(R.id.tv_titl);
        tv_des = findViewById(R.id.tv_des);
        cb_box = findViewById(R.id.cb_box);

        //获取自定义以及原生属性的操作，写在此处，AttributeSet attrs对象中获取
        initAttrs(attrs);

        tv_title.setText(mDestitle);
    }

    private void initAttrs(AttributeSet attrs) {
        /*Log.d(TAG, "initAttrs: "+attrs.getAttributeCount());
        //获取属性名称以及属性值
        for (int i = 0;i<attrs.getAttributeCount();i++){

        }*/
        //通过命名空间获取指定属性值的值
        mDestitle = attrs.getAttributeValue(NAME_SPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAME_SPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAME_SPACE, "deson");
    }

    public boolean isCheck() {
        return cb_box.isChecked();
    }

    /**
     * @param isCheck 是否作为开启的变量，由点击过程中去传递
     */
    public void setCheck(boolean isCheck) {
        cb_box.setChecked(isCheck);
        if (isCheck){
            tv_des.setText(mDeson);
        }else{
            tv_des.setText(mDesoff);
        }
    }
}
