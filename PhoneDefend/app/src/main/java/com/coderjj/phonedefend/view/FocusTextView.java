package com.coderjj.phonedefend.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2019/5/8.
 * 能够获取焦点的自定义TextView
 */

@SuppressLint("AppCompatCustomView")
public class FocusTextView extends TextView {
    //java代码中创建控件
    public FocusTextView(Context context) {
        super(context);
    }
    //由系统调用
    public FocusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    //由系统调用
    public FocusTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //重写获取焦点的方法,系统调用
    @Override
    public boolean isFocused() {
//        return super.isFocused();
        return true;
    }
}
