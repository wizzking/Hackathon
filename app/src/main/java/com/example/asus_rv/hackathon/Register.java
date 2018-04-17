package com.example.asus_rv.hackathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import java.net.URISyntaxException;

public class Register extends AppCompatActivity {
    private Socket mSocket;
    private String tipo;
    public void conectar ()
    {
        EditText DataName = (EditText) findViewById(R.id.RNombre);
        String FinalDataName = DataName.getText().toString();

        EditText DataLastName1 = (EditText) findViewById(R.id.RLastName1);
        String FinalDataLastName1 = DataLastName1.getText().toString();

        EditText DataLastName2 = (EditText) findViewById(R.id.RLastName2);
        String FinalDataLastName2 = DataLastName2.getText().toString();

        EditText DataPass = (EditText) findViewById(R.id.RPass);
        String FinalDataPass = DataPass.getText().toString();

        EditText DataRPass = (EditText) findViewById(R.id.RRPass);
        String FinalDataRPass = DataRPass.getText().toString();

        EditText DataCorreo = (EditText) findViewById(R.id.RCorreo);
        String FinalDataCorreo = DataCorreo.getText().toString();

        if(TextUtils.isEmpty(FinalDataName) || TextUtils.isEmpty(FinalDataLastName1)
                || TextUtils.isEmpty(FinalDataLastName2) || TextUtils.isEmpty(FinalDataPass)
                || TextUtils.isEmpty(FinalDataRPass) || TextUtils.isEmpty(FinalDataCorreo)){
            Toast.makeText(Register.this, "Favor de Completar los campos.", Toast.LENGTH_SHORT).show();
        }
        else{
            if (FinalDataPass.equals(FinalDataRPass))
            {
                try {
                    mSocket = IO.socket("http://192.168.8.27:90");
                } catch (URISyntaxException e) {}

                mSocket.on("getResponse",getResponseRegister);
                mSocket.on("sendDatosUserRegister",sendDatosUserRegister);
                mSocket.connect();

            }
            else
            {
                Toast.makeText(Register.this, "Favor de ingresar contraseñas iguales.", Toast.LENGTH_SHORT).show();
                mSocket.disconnect();

            }

        }
    }

    public Emitter.Listener getResponseRegister=new Emitter.Listener(){
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    EditText DataName = (EditText) findViewById(R.id.RNombre);
                    String FinalDataName = DataName.getText().toString();

                    EditText DataLastName1 = (EditText) findViewById(R.id.RLastName1);
                    String FinalDataLastName1 = DataLastName1.getText().toString();

                    EditText DataLastName2 = (EditText) findViewById(R.id.RLastName2);
                    String FinalDataLastName2 = DataLastName2.getText().toString();

                    EditText DataPass = (EditText) findViewById(R.id.RPass);
                    String FinalDataPass = DataPass.getText().toString();


                    EditText DataCorreo = (EditText) findViewById(R.id.RCorreo);
                    String FinalDataCorreo = DataCorreo.getText().toString();

                    SocketData cont= new SocketData();
                    cont.RNombre        = FinalDataName;
                    cont.RLastName1    = FinalDataLastName1;
                    cont.RLastName2       = FinalDataLastName2;
                    cont.RPassword   = FinalDataPass;
                    cont.RLatitud   = "123654";
                    cont.RCombo   = tipo;
                    cont.RCorreo = FinalDataCorreo;
                    cont.RLongitud   = "-78545";
                    Gson gson=new Gson();
                    mSocket.emit("sendDatosUserRegister",gson.toJson(cont));
                    //String g = args[0].toString();
                    //Toast.makeText(Login.this, g, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    public Emitter.Listener sendDatosUserRegister=new Emitter.Listener(){
        public void call(final Object... args){
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    Gson gson=new Gson();
                    SocketData msg=gson.fromJson(args[0].toString(),SocketData.class);
                    Toast.makeText(Register.this, msg.Respuesta, Toast.LENGTH_SHORT).show();

                    mSocket.disconnect();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] valores = {"Person","Fijo"};
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                tipo = (String) adapterView.getItemAtPosition(position);
               // Toast.makeText(adapterView.getContext(), tipo, Toast.LENGTH_SHORT).show();

            }


            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio

            }
        });

        Button Ok_ = (Button) findViewById(R.id.Ok);
        Button Exit_ = (Button) findViewById(R.id.Exit);



        Ok_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                conectar();
            }
        });







        Exit_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    finish();
            }

        });



    }
}
