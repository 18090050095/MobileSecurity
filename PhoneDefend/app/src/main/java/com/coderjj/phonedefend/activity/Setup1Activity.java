package com.coderjj.phonedefend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.coderjj.phonedefend.R;

public class Setup1Activity extends BaseStepActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
        initView();
    }

    private void initView() {
        findViewById(R.id.bt_setup1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNext();
            }
        });
    }

    @Override
    public void goToPre() {
    }

    @Override
    public void goToNext() {
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}
