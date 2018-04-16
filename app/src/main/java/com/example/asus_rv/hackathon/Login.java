package com.example.asus_rv.hackathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        Button Home_ = (Button) findViewById(R.id.Home);
        Button Register_ = (Button) findViewById(R.id.Registre);



        Home_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent OpenMyHome=new Intent(Login.this,Home.class);

                startActivity(OpenMyHome);
            }
        });


        Register_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent OpenMyRegister=new Intent(Login.this,Register.class);

                startActivity(OpenMyRegister);
            }
        });


    }
}
