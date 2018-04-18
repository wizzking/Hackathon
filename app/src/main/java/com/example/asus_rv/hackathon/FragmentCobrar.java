package com.example.asus_rv.hackathon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

import java.net.URISyntaxException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCobrar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCobrar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCobrar extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
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

            mSocket.on("getResponse",getResponseCobrar);
            //mSocket.on("sendDatosUserPago",sendDatosUserPago);
            mSocket.connect();
        }
   // }

    public Emitter.Listener getResponseCobrar=new Emitter.Listener(){
        public void call(final Object... args){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run()
                {


                    EditText DataSeatrch = (EditText) getActivity().findViewById(R.id.CantEmail);
                    String Serach = DataSeatrch.getText().toString();

                    EditText DataPago = (EditText) getActivity().findViewById(R.id.CantPago);
                    String Pago = DataPago.getText().toString();

                    SocketData cont= new SocketData();
                    cont.EmailSerach    = Serach;
                    cont.SendPago = Pago;
                    cont.pasoEmail = cont.EmailUser;
                    Gson gson=new Gson();
                    mSocket.emit("sendDatosUserCobro",gson.toJson(cont));
                }
            });
        }
    };



    /*public Emitter.Listener sendDatosUserPago=new Emitter.Listener(){
        public void call(final Object... args){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    mSocket.emit("sendDatosUserPago",null);
                    Gson gson=new Gson();
                    SocketData msg=gson.fromJson(args[0].toString(),SocketData.class);
                    Toast.makeText(getActivity(), msg.Respuesta, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(Login.this, msg.Respuesta, Toast.LENGTH_SHORT).show();
                    //mSocket.disconnect();


                }
            });
        }
    };*/

    public FragmentCobrar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCobrar.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCobrar newInstance(String param1, String param2) {
        FragmentCobrar fragment = new FragmentCobrar();
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
        View view = inflater.inflate(R.layout.fragment_fragment_cobrar, container, false);

        Button PagoNow = (Button) view.findViewById(R.id.PageNow);

        PagoNow.setOnClickListener(this);


        return view;

    }


    @Override
    public void onClick(View v) {
        //Toast.makeText(getActivity(), "No se encontro la cuentaad", Toast.LENGTH_SHORT).show();
        conectar ();
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



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
