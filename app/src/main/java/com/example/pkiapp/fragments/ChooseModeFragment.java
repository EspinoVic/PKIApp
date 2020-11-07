package com.example.pkiapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pkiapp.R;

public class ChooseModeFragment extends Fragment {

    Button btn_server;
    Button btn_client;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_choose_mode, container, false);

        this.btn_client = rootView.findViewById(R.id.btn_client);
        this.btn_server = rootView.findViewById(R.id.btn_servidor);

        this.btn_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_chooseModeFragment_to_clientFragment);

            }
        });

        this.btn_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_chooseModeFragment_to_serverFragment);
            }
        });

        return rootView;
    }
}