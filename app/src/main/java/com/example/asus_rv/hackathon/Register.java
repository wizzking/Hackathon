package com.example.asus_rv.hackathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        Button Ok_ = (Button) findViewById(R.id.Ok);
        Button Exit_ = (Button) findViewById(R.id.Exit);



        Ok_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });







        Exit_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    finish();
            }

        });



    }
}
