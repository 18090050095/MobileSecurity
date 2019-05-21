package com.coderjj.phonedefend.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.coderjj.phonedefend.R;

public abstract class BaseStepActivity extends Activity {

    private GestureDetector mGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGesture();
    }

    private void initGesture() {
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // 判断纵向坐标是否超过100，提示手势不正确
                if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
                    Toast.makeText(getApplicationContext(), "滑动的角度不能贴斜哦！", Toast.LENGTH_SHORT).show();
                    return true;
                }
                // 判断横向滑动速度是否太慢了
                if (Math.abs(velocityX) < 100) {
                    Toast.makeText(getApplicationContext(), "滑动的太慢了哦！", Toast.LENGTH_SHORT).show();
                    return true;
                }
                // 向右滑动，展示上一页
                if (e2.getRawX() - e1.getRawX() > 200) {
                    goToPre();
                    return true;
                }
                // 向左滑动，展示下一页
                if (e1.getRawX() - e2.getRawX() > 200) {
                    goToNext();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public abstract void goToPre();
    public abstract void goToNext();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
