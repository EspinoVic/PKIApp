package com.example.pkiapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    Button btn_send_msj;
    Button btnConectarServer ;

    EditText txt_msj_a_codificar;
    EditText txt_key_para_codificar;
    TextView lbl_msj_codificado;


    EditText txt_ip_server;
    EditText txt_puerto;

    Socket socketForSending;
    OutputStream outputStreamSending;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_client, container, false);

        this.txt_msj_a_codificar = viewRoot.findViewById(R.id.txt_msj_a_codificar);
        this.txt_key_para_codificar = viewRoot.findViewById(R.id.txt_key_para_codificar);
        this.lbl_msj_codificado = viewRoot.findViewById(R.id.lbl_msj_codificado);

        this.btn_send_msj = viewRoot.findViewById(R.id.btn_send_msj);
        this.btn_send_msj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
/*
                    outputStreamSending.write(codificarMsj(txt_msj_a_codificar.getText().toString()).getBytes());
*/

                    outputStreamSending = socketForSending.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStreamSending);
                    dataOutputStream.writeUTF(codificarMsj(txt_msj_a_codificar.getText().toString()));
                    dataOutputStream.flush();
                    /*dataOutputStream.close();*/
                } catch (IOException e) {
                    Snackbar.make(view,"Cant send messages.",Snackbar.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });



        this.txt_ip_server = viewRoot.findViewById(R.id.txt_ip_server);
        this.txt_puerto = viewRoot.findViewById(R.id.txt_puerto);

        this.btnConectarServer = viewRoot.findViewById(R.id.btn_connect);
        this.btnConectarServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if(socketForSending != null)
                    return;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socketForSending = new Socket(txt_ip_server.getText().toString(),Integer.parseInt(txt_puerto.getText().toString()));
                            outputStreamSending = socketForSending.getOutputStream();
                            Snackbar.make(view,"Connected.",Snackbar.LENGTH_SHORT).show();


                        } catch (IOException e) {
                            e.printStackTrace();
                            Snackbar.make(view,"Cant connect that address.",Snackbar.LENGTH_SHORT).show();

                        }
                    }
                }).start();



            }
        });

        return viewRoot;
    }

    private String codificarMsj(String toString) {
        return toString;
    }
}