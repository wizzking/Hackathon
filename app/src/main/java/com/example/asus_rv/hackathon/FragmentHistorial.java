package com.example.asus_rv.hackathon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.net.URISyntaxException;


public class FragmentHistorial extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
       ListView lista;
       String[] nombreLista ={"Paulina Vargas","Astrik Adrian","Fernanda Silva"};
       String[] nombreLista2 ={"50 pesos","100 pesos", "300 pesos"};

    private Socket mSocket;


    public void conectar ()
    {
        // String FinalUser2 = DataUser2.getText().toString();
        // String FinalPass2 = DataPass2.getText().toString();

        //if(TextUtils.isEmpty(FinalUser2) || TextUtils.isEmpty(FinalPass2)){
        // Toast.makeText(Login.this, "Favor de Completar los campos.", Toast.LENGTH_SHORT).show();
        /*}
        else{*/
        try {
            mSocket = IO.socket("http://192.168.8.27:90");
        } catch (URISyntaxException e) {}

        mSocket.on("getResponse",getDataLogsFun);
        mSocket.on("getDataLogs",getDataLogs);
        mSocket.connect();
    }
    // }


    public Emitter.Listener getDataLogsFun=new Emitter.Listener(){
        public void call(final Object... args){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    SocketData cont= new SocketData();
                    cont.pasoEmail = cont.EmailUser;
                    Gson gson=new Gson();
                    mSocket.emit("getDataLogs",gson.toJson(cont));
                }
            });
        }
    };


    public Emitter.Listener getDataLogs=new Emitter.Listener(){
        public void call(final Object... args){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    Gson gson=new Gson();
                    SocketData msg=gson.fromJson(args[0].toString(),SocketData.class);
                    Toast.makeText(getActivity(), msg.Respuesta, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(Login.this, msg.Respuesta, Toast.LENGTH_SHORT).show();
                    mSocket.disconnect();


                }
            });
        }
    };

    public FragmentHistorial() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentHistorial newInstance(String param1, String param2) {
        FragmentHistorial fragment = new FragmentHistorial();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_historial, container, false);

        ///conectar();


        lista = (ListView)view.findViewById(R.id.lista);
        ArrayAdapter<String> adaptador;
        adaptador=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,nombreLista);
        lista.setAdapter(adaptador);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
