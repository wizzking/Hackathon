package com.example.asus_rv.hackathon;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;

/**
 * Created by WINDOWS-PC on 17/04/2018.
 */

public class Adapterlist extends BaseAdapter {
    String[]  nombres;
    String [] costos;
    FragmentHistorial contexto;
    public Adapterlist(FragmentHistorial historial,String[] nombreAbarrote,String[] costo){
        nombres= nombreAbarrote;
        costos = costo;
        contexto = historial;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
public class Holder{
        EditText title;
        EditText subTitle;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
    Holder holder = new Holder();

    View fila;
    return null;
    }
}
