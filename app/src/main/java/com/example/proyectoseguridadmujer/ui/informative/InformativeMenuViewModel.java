package com.example.proyectoseguridadmujer.ui.informative;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InformativeMenuViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public InformativeMenuViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is informative fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}