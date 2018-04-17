package com.example.asus_rv.hackathon;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,FragmentPagar.OnFragmentInteractionListener,FragmentCobrar.OnFragmentInteractionListener,FragmentHistorial.OnFragmentInteractionListener,FragmentAjustes.OnFragmentInteractionListener,FragmentSalir.OnFragmentInteractionListener {
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
    }
    private void esconderItem(){
        NavigationView navegationView = (NavigationView)findViewById(R.id.nav_view);
        Menu nav_Menu=navegationView.getMenu();
        nav_Menu.findItem(R.id.nav_cobrar).setVisible(false);
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
            getSupportFragmentManager().beginTransaction().replace(R.id.conte,fracment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
