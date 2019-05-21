package com.coderjj.phonedefend.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.coderjj.phonedefend.R;
import com.coderjj.phonedefend.utils.ConstantValue;
import com.coderjj.phonedefend.utils.SpUtil;

public class ToastLocationActivity extends Activity {

    private ImageView iv_drag;
    private Button bt_top;
    private Button bt_bottom;
    private int mScreenWidth;
    private int mScreenHeight;
    private long[] mHint = new long[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_location);

        initView();
    }

    private void initView() {
        iv_drag = findViewById(R.id.iv_drag);
        bt_top = findViewById(R.id.bt_top);
        bt_bottom = findViewById(R.id.bt_bottom);

        /*
        * 获取屏幕宽高
        * 注：
        * 屏幕宽高为可显示区域的长度
        * 控件的getBottom()不包括通知栏的高度
        * */
        mScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
        mScreenHeight = getWindowManager().getDefaultDisplay().getHeight();

        //初始化iv_drag位置
        /*iv_drag.layout(locationX, locationY, locationX, locationY);*/
        ininDrag();

        //双击事件设置
        iv_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHint, 1, mHint, 0, mHint.length - 1);
                mHint[mHint.length - 1] = System.currentTimeMillis();
                if (mHint[mHint.length - 1] - mHint[0] < 500) {
                    int left = mScreenWidth/2 - iv_drag.getWidth()/2;
                    int right = mScreenWidth/2 + iv_drag.getWidth()/2;
                    int top = mScreenHeight/2 - iv_drag.getHeight()/2;
                    int bottom = mScreenHeight/2 + iv_drag.getHeight()/2;
                    iv_drag.layout(left,top,right,bottom);
                    SpUtil.putInt(getApplication(), ConstantValue.LOCATION_X, iv_drag.getLeft());
                    SpUtil.putInt(getApplication(), ConstantValue.LOCATION_Y, iv_drag.getTop());
                }
            }
        });

        //监听某一个控件的拖拽过程(按下1，移动n，抬起1)
        iv_drag.setOnTouchListener(new View.OnTouchListener() {
            private int startY;
            private int startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();

                        int disX = moveX - startX;
                        int disY = moveY - startY;

                        int left = iv_drag.getLeft() + disX;
                        int right = iv_drag.getRight() + disX;
                        int top = iv_drag.getTop() + disY;
                        int bottom = iv_drag.getBottom() + disY;

                        // 防止坐标越界
                        if (left < 0 || right > mScreenWidth) {
                            break;
                        }
                        if (bottom > mScreenHeight - iv_drag.getHeight() - 35) {
                            break;
                        }
                        if (top < 0) {
                            break;
                        }

                        // 根据drag的位置显示或隐藏上或者下提示框
                        if (top > mScreenHeight / 2) {
                            bt_top.setVisibility(View.VISIBLE);
                            bt_bottom.setVisibility(View.INVISIBLE);
                        } else {
                            bt_top.setVisibility(View.INVISIBLE);
                            bt_bottom.setVisibility(View.VISIBLE);
                        }

                        iv_drag.layout(left, top, right, bottom);

                        //重置起始坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //存储移动到的位置
                        SpUtil.putInt(getApplication(), ConstantValue.LOCATION_X, iv_drag.getLeft());
                        SpUtil.putInt(getApplication(), ConstantValue.LOCATION_Y, iv_drag.getTop());
                        break;
                }
                /*只设置setOnTouchListener返回true才能对触摸事件生效
                 *若设置了setOnClickListener则要返回false两个事件才都会生效
                 */
                return false;
            }
        });
    }

    /**
     * drag的位置回显
     */
    public void ininDrag() {
        // 拿到配置文件中记录的坐标
        int locationX = SpUtil.getInt(getApplication(), ConstantValue.LOCATION_X, 0);
        int locationY = SpUtil.getInt(getApplication(), ConstantValue.LOCATION_Y, 0);
        // 根据drag的位置显示或隐藏上或者下提示框
        if (locationY > mScreenHeight / 2) {
            bt_top.setVisibility(View.VISIBLE);
            bt_bottom.setVisibility(View.INVISIBLE);
        } else {
            bt_top.setVisibility(View.INVISIBLE);
            bt_bottom.setVisibility(View.VISIBLE);
        }
        // 拿到布局参数信息
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_drag
                .getLayoutParams();
        // 更新布局位置
        // 设置左边距
        layoutParams.leftMargin = locationX;
        // 设置上边距
        layoutParams.topMargin = locationY;
        // 重新设置位置
        iv_drag.setLayoutParams(layoutParams);
    }
}
