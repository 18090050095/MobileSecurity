package com.coderjj.phonedefend.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.coderjj.phonedefend.R;

public class Setup3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initView();
    }

    private void initView() {

        findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
                startActivityForResult(intent,1);
            }
        });
        findViewById(R.id.bt_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
        findViewById(R.id.bt_pre).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
