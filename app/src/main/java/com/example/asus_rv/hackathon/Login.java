package com.example.asus_rv.hackathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;


import android.app.Application;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import java.net.URISyntaxException;


public class Login extends AppCompatActivity {

    private Socket mSocket;
    public void conectar ()
    {
        EditText DataUser2 = (EditText) findViewById(R.id.User);
        String FinalUser2 = DataUser2.getText().toString();

        EditText DataPass2 = (EditText) findViewById(R.id.Pass);
        String FinalPass2 = DataPass2.getText().toString();

        if(TextUtils.isEmpty(FinalUser2) || TextUtils.isEmpty(FinalPass2)){
            Toast.makeText(Login.this, "Favor de Completar los campos.", Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                mSocket = IO.socket("http://192.168.8.27:90");
            } catch (URISyntaxException e) {}

            mSocket.on("getResponse",getResponseLogin);
            mSocket.on("sendDatosUser",sendDatosUser);
            mSocket.connect();
        }
    }

    public Emitter.Listener getResponseLogin=new Emitter.Listener(){
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    EditText DataUser = (EditText) findViewById(R.id.User);
                    String FinalUser = DataUser.getText().toString();

                    EditText DataPass = (EditText) findViewById(R.id.Pass);
                    String FinalPass = DataPass.getText().toString();

                    SocketData cont= new SocketData();
                    cont.User        = FinalUser;
                    cont.Password    = FinalPass;
                    cont.Status       = "1";
                    cont.Type   = "Person";
                    Gson gson=new Gson();
                    mSocket.emit("sendDatosUser",gson.toJson(cont));
                    //String g = args[0].toString();
                    //Toast.makeText(Login.this, g, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    public Emitter.Listener sendDatosUser=new Emitter.Listener(){
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    Gson gson=new Gson();
                    SocketData msg=gson.fromJson(args[0].toString(),SocketData.class);
                    //Toast.makeText(Login.this, msg.Respuesta, Toast.LENGTH_SHORT).show();
                    if (msg.Respuesta.equals("Persona"))
                    {
                        Toast.makeText(Login.this, "welcome Persona", Toast.LENGTH_SHORT).show();
                        mSocket.disconnect();
                       Intent OpenMyHome=new Intent(Login.this,Home.class);
                      startActivity(OpenMyHome);
                    }
                    else if (msg.Respuesta.equals("Fijo"))
                    {
                        Toast.makeText(Login.this, "Welcome Fijo", Toast.LENGTH_SHORT).show();
                        mSocket.disconnect();
                       Intent OpenMyHome2=new Intent(Login.this,Home.class);
                       startActivity(OpenMyHome2);
                    }
                    else
                    {
                        Toast.makeText(Login.this, "No se encontro la cuentaad", Toast.LENGTH_SHORT).show();
                    }
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
