package com.example.pkiapp.fragments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ServerViewModel extends ViewModel {

    MutableLiveData<String> infoIPLiveData;
    MutableLiveData<String> msjCodifiedLiveData;

    public ServerViewModel() {
        this.infoIPLiveData = new MutableLiveData<>();
        this.msjCodifiedLiveData = new MutableLiveData<>();


    }

    public MutableLiveData<String> getInfoIPLiveData() {
        return infoIPLiveData;
    }

    public MutableLiveData<String> getMsjCodifiedLiveData() {
        return msjCodifiedLiveData;
    }
}
