package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class profile extends AppCompatActivity {
    TextView na,mob,pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        na=findViewById(R.id.na);
        mob=findViewById(R.id.mob);
        pwd=findViewById(R.id.pwd);

        SharedPreferences sh=getSharedPreferences("profile",MODE_PRIVATE);
        String n=sh.getString("name","Name");
        String p=sh.getString("phone","Phone");
        String pd=sh.getString("password","Password");

        na.setText(n);
        mob.setText(p);
        pwd.setText(pd);
    }
}