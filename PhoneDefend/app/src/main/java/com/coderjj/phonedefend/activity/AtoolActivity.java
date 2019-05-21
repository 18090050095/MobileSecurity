package com.coderjj.phonedefend.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.coderjj.phonedefend.R;

public class AtoolActivity extends AppCompatActivity {

    private TextView mQueryPhoneAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);

        //归属地查询
        initPhoneAddress();
    }

    private void initPhoneAddress() {
        mQueryPhoneAdress = findViewById(R.id.query_phone_adress);
        mQueryPhoneAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QueryAdressActivity.class);
                startActivity(intent);
            }
        });
    }
}
