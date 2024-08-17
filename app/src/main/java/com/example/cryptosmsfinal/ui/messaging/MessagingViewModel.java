package com.example.cryptosmsfinal.ui.messaging;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MessagingViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MessagingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}