package com.example.asus_rv.hackathon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus_rv.hackathon.Config.Config;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.net.URISyntaxException;

import static android.app.Activity.RESULT_OK;


public class FragmentPagar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    Button btnPayNow;
    EditText edtAmout;
    String amount = "";


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

        mSocket.on("getResponse",getResponsePagar);
        mSocket.on("sendDatosUserPago",sendDatosUserPago);
        mSocket.connect();
    }
    // }

    public Emitter.Listener getResponsePagar=new Emitter.Listener(){
        public void call(final Object... args){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run()
                {


                    EditText DataSeatrch = (EditText) getActivity().findViewById(R.id.CantEmail);
                    String Serach = DataSeatrch.getText().toString();


                    SocketData cont= new SocketData();
                    cont.EmailSerach    = Serach;
                    cont.pasoEmail = cont.EmailUser;
                    Gson gson=new Gson();
                    mSocket.emit("sendDatosUserPago",gson.toJson(cont));
                }
            });
        }
    };

    public Emitter.Listener sendDatosUserPago=new Emitter.Listener(){
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



    public FragmentPagar() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentPagar newInstance(String param1, String param2) {
        FragmentPagar fragment = new FragmentPagar();
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
    public void onDestroy(){
        getActivity().stopService(new Intent(getActivity(),PayPalService.class));
        super.onDestroy();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_pagar, container, false);
        //Start PAYPAL Service
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        getActivity().startService(intent);

        btnPayNow = (Button) view.findViewById(R.id.btnPayNow);
        //edtAmout = (EditText) view.findViewById(R.id.CantEmail);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //processPayment();
                conectar();
            }
        });
        return view;
    }


    private void processPayment() {
        amount = edtAmout.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"MXN","Motivos o tienda a pagar",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        if (requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                PaymentConfirmation confirmation  = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                if (confirmation != null){
                    try{
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(getActivity(),PaymentDetails.class)
                                .putExtra("PaymentDetails",paymentDetails).putExtra("PaymentAmount",amount)
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(getActivity(),"Pago cancelado",Toast.LENGTH_SHORT).show();
            }
        }else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(getActivity(),"Invalido",Toast.LENGTH_SHORT).show();
        }

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
