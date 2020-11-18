package com.example.pkiapp.fragments;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pkiapp.Cesar;
import com.example.pkiapp.R;
import com.example.pkiapp.ServerViewModel;
import com.example.pkiapp.Transposicion;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static android.content.Context.WIFI_SERVICE;

public class ServerFragment extends Fragment {


    ServerSocket socketForReceiving;
    ProgressBar progress_circular_opnening_connection;

    Button btnDecodificar;
    Button btn_open_connection;
    TextView lbl_connectIP;
    TextView lbl_msj_recibido_codificado;

    EditText txt_msj_decodificado;
    EditText txt_key_decodificar;

    ServerViewModel serverViewModel;

    Spinner spinnerDecryptMethod;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_server, container, false);

        this.spinnerDecryptMethod = viewRoot.findViewById(R.id.spinner_encrypt_method);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.encrypt_method, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinnerDecryptMethod .setAdapter(adapter);
        this.spinnerDecryptMethod.setSelection(0);


        this.lbl_connectIP = viewRoot.findViewById(R.id.lbl_connectIP);
        this.lbl_msj_recibido_codificado = viewRoot.findViewById(R.id.lbl_msj_recibido_codificado);

        this.btn_open_connection = viewRoot.findViewById(R.id.btn_open_connection);
        this.btn_open_connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(socketForReceiving != null)
                            return;
                        try {
                            socketForReceiving = new ServerSocket(1254);
                            Snackbar.make(view,"Conexión abierta",Snackbar.LENGTH_SHORT).show();

                            InetAddress localHost = InetAddress.getLocalHost();
                            serverViewModel.getInfoIPLiveData().postValue("Connect to: " +
                                    /*socketForReceiving.getLocalSocketAddress().toString()
                                    + socketForReceiving.getLocalPort()*/
                                    getLocalIpAddress() +":" + socketForReceiving.getLocalPort()
                            );
                            //wait until a connection exist
                            Socket accept = socketForReceiving.accept();
                            accept.setReuseAddress(true);
                            /*while(conexionAbierta){*/

                                BufferedReader in = new BufferedReader(
                                        new InputStreamReader(accept.getInputStream()));
                                char data[] = new char[254];
                                String mensaje="";
                                //mientras reciba data, la irá appendiando
                                int readLength = in.read(data);
                                while(readLength!=-1){
                                    mensaje += String.valueOf(data,0,readLength);
                                    data = new char[254];
                                    serverViewModel.getMsjCodifiedLiveData().postValue(String.valueOf(mensaje));
                                    mensaje = "";
                                    readLength = in.read(data);
                                }

                           // }

                        } catch (IOException e) {
                            e.printStackTrace();
                            socketForReceiving = null;
                            Snackbar.make(view,"Error abriendo conexión",Snackbar.LENGTH_SHORT).show();

                        }


                    }
                }).start();

            }
        });

        txt_msj_decodificado = viewRoot.findViewById(R.id.txt_msj_decodificado);
        txt_key_decodificar = viewRoot.findViewById(R.id.txt_key_decodificar);

        this.btnDecodificar = viewRoot.findViewById(R.id.btn_decodificar);
        this.btnDecodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messgge= getMsgDecifrado(lbl_msj_recibido_codificado.getText().toString());
                txt_msj_decodificado.setText(messgge);
            }
        });

        this.progress_circular_opnening_connection = viewRoot.findViewById(R.id.progress_circular_opnening_connection);

        this.serverViewModel = new ViewModelProvider(this).get(ServerViewModel.class);
        this.serverViewModel.getInfoIPLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String infoToConnect) {
                if(infoToConnect!=null){
                    lbl_connectIP.setText(infoToConnect);
                }
            }
        });

        this.serverViewModel.getMsjCodifiedLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String messgge) {
                if(messgge!=null){
                    lbl_msj_recibido_codificado.setText(messgge);
                    txt_msj_decodificado.setText(
                            getMsgDecifrado(messgge)
                    );
                }

            }
        });

        return viewRoot;
    }


    public String getMsgDecifrado(String messgge){
        /**
         * if X method select, do something an return
         * otherwise
         * do other thing
         */

        String methodSelected = (String) spinnerDecryptMethod.getSelectedItem();
        String output = "ERROR";
        switch (methodSelected){
            case "Cesar":
                output = Cesar.desencriptar(messgge,Integer.parseInt(txt_key_decodificar.getText().toString()));

                break;
            case "transposicion_simetrica":
            case "transposicion_asimetrica":
                try {
                    output = Transposicion.decifrar(messgge);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        return output;
    }
    private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
        if(wifiManager!= null){
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipInt = wifiInfo.getIpAddress();
            return InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).getHostAddress();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(socketForReceiving!=null) {
            try {
                socketForReceiving.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}