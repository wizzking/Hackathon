package com.example.asus_rv.hackathon;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.asus_rv.hackathon.SocketData.TypeDatoOfUser;

public class Home extends AppCompatActivity
        implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener,FragmentPagar.OnFragmentInteractionListener,FragmentCobrar.OnFragmentInteractionListener,FragmentHistorial.OnFragmentInteractionListener,FragmentAjustes.OnFragmentInteractionListener,FragmentSalir.OnFragmentInteractionListener{
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        SocketData cont= new SocketData();
        TextView textView = (TextView) findViewById(R.id.txtsock);
        textView.setText(cont.SocketIdUser);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        esconderItem();

        Button mapa= (Button) findViewById(R.id.Mapa);

        mapa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mapa();
            }
        });

    }

    public void mapa(){
        Intent OpenMyRegister=new Intent(Home.this,Mapa.class);
        startActivity(OpenMyRegister);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(10, 10)).title("Tienda 1"));
        map.addMarker(new MarkerOptions().position(new LatLng(20, 20)).title("Tienda 2"));
        map.addMarker(new MarkerOptions().position(new LatLng(30, 30)).title("Tienda 3"));
        map.addMarker(new MarkerOptions().position(new LatLng(40, 40)).title("Tienda 4"));
        map.addMarker(new MarkerOptions().position(new LatLng(50, 50)).title("Tienda 5"));
    }

    private void esconderItem(){
        SocketData cont= new SocketData();
        NavigationView navegationView = (NavigationView)findViewById(R.id.nav_view);
        Menu nav_Menu=navegationView.getMenu();
        if(cont.TypeDatoOfUser.equals("Fijo")){
            nav_Menu.findItem(R.id.nav_pagar).setVisible(false);
        }
        else if(cont.TypeDatoOfUser.equals("Person")){
            nav_Menu.findItem(R.id.nav_cobrar).setVisible(false);

        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
            android.support.v4.app.Fragment fracment = null;
        Boolean fracmentSelected = false;
        if (id == R.id.nav_cobrar) {
            fracment = new FragmentCobrar();
            fracmentSelected=true;
        }else if (id == R.id.nav_pagar) {
            fracment = new FragmentPagar();
            fracmentSelected=true;
        }else if (id == R.id.nav_historial) {
            fracment = new FragmentHistorial();
            fracmentSelected=true;
        }
        else if (id == R.id.nav_ajustes) {
            fracment = new FragmentAjustes();
            fracmentSelected=true;

        }else if(id == R.id.nav_salir){
            fracment = new FragmentSalir();
            fracmentSelected=true;
        }
        if(fracmentSelected){
            getSupportFragmentManager().beginTransaction().replace(R.id.conte,fracment).addToBackStack(null).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
