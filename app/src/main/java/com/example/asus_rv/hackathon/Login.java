package com.example.asus_rv.hackathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;


import android.app.Application;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import java.net.URISyntaxException;


public class Login extends AppCompatActivity {

    private Socket mSocket;
    public void conectar ()
    {
        try {
            mSocket = IO.socket("http://192.168.8.27:90");
        } catch (URISyntaxException e) {}

        mSocket.on("getResponse",getResponse);

        mSocket.connect();

    }

    public Emitter.Listener getResponse=new Emitter.Listener(){
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SocketData cont= new SocketData();
                    cont.nip        = "asd";
                    cont.nombreT    = "asd";
                    cont.sexo       = "asd";
                    cont.telefono   = "asd";
                    Gson gson=new Gson();
                    mSocket.emit("sendDatosUser",gson.toJson(cont));
                }
            });
        }
    };



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button Home_ = (Button) findViewById(R.id.Home);
        Button Register_ = (Button) findViewById(R.id.Registre);



        Home_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // Intent OpenMyHome=new Intent(Login.this,Home.class);

               // startActivity(OpenMyHome);
                conectar();
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
