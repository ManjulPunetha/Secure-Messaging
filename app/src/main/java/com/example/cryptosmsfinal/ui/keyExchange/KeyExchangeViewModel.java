package com.example.cryptosmsfinal.ui.keyExchange;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KeyExchangeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public KeyExchangeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}