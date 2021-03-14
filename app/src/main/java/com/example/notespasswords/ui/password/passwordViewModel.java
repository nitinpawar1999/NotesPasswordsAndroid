package com.example.notespasswords.ui.password;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class passwordViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public passwordViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is password fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}