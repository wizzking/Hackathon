package com.example.asus_rv.hackathon;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;

import java.net.URISyntaxException;

public class Register extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {


    private Socket msocket;{
        try{
            msocket= IO.socket("http://172.16.3.160:90");
        }catch(URISyntaxException e){}
    }


    private static final String LOGTAG = "android-localizacion";

    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;

    private GoogleApiClient apiClient;

    private String lblLatitud;
    private String lblLongitud;

    private LocationRequest locRequest;

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
                    cont.RLatitud   =  lblLatitud;
                    cont.RCombo   = tipo;
                    cont.RCorreo = FinalDataCorreo;
                    cont.RLongitud   = lblLongitud;
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



        Ok_.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                conectar();
            }
        });

        //Construcción cliente API Google
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        enableLocationUpdates();


    }


    private void enableLocationUpdates() {

        locRequest = new LocationRequest();
        locRequest.setInterval(2000);
        locRequest.setFastestInterval(1000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest locSettingsRequest =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locRequest)
                        .build();

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        apiClient, locSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.i(LOGTAG, "Configuración correcta");
                        startLocationUpdates();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.i(LOGTAG, "Se requiere actuación del usuario");
                            status.startResolutionForResult(Register.this, PETICION_CONFIG_UBICACION);
                        } catch (IntentSender.SendIntentException e) {

                            Log.i(LOGTAG, "Error al intentar solucionar configuración de ubicación");
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(LOGTAG, "No se puede cumplir la configuración de ubicación necesaria");

                        break;
                }
            }
        });
    }

    private void disableLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(
                apiClient, this);

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(Register.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //Ojo: estamos suponiendo que ya tenemos concedido el permiso.
            //Sería recomendable implementar la posible petición en caso de no tenerlo.

            Log.i(LOGTAG, "Inicio de recepción de ubicaciones");

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    apiClient, locRequest, Register.this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.

        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            updateUI(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services

        Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    private void updateUI(Location loc) {
        if (loc != null) {
            lblLatitud = String.valueOf(loc.getLatitude());
            lblLongitud = String.valueOf(loc.getLongitude());

            Toast.makeText(this,"Mensaje Enviado",Toast.LENGTH_SHORT).show();
        } else {
            lblLatitud = "Latitud: (desconocida)";
            lblLongitud = "Longitud: (desconocida)";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);

            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(LOGTAG, "Permiso denegado");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PETICION_CONFIG_UBICACION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(LOGTAG, "El usuario no ha realizado los cambios de configuración necesarios");
                        break;
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i(LOGTAG, "Recibida nueva ubicación!");

        //Mostramos la nueva ubicación recibida
        updateUI(location);
    }



}
