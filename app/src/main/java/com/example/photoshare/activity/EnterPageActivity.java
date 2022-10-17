package com.example.photoshare.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalwork.R;

public class EnterPageActivity extends AppCompatActivity {

    private TextView tit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_page);

        tit = findViewById(R.id.enterTitle);
        tit.setText("安卓Android");
        tit.setTextColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(EnterPageActivity.this, LoginActivity.class));
            }
        }, 3000);


    }
}
