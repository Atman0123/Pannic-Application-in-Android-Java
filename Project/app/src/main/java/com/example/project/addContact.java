package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;



public class addContact extends AppCompatActivity {
    Button btn;
    EditText cnt;
    TextView textView3, panic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        btn = findViewById(R.id.btn);
        cnt = findViewById(R.id.cnt);
        panic = findViewById(R.id.panic);

        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num= cnt.getText().toString().trim();
                SharedPreferences sh=getSharedPreferences("con",MODE_PRIVATE);
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("contact",num);
                ed.apply();
                Toast.makeText(addContact.this, "Number Added", Toast.LENGTH_SHORT).show();

            }
        });
//        SharedPreferences sh=getSharedPreferences("con",MODE_PRIVATE);
//       String num=sh.getString("name","No Value as of now");
//        panic.setText(num);



    }
}