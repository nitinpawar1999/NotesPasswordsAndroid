package com.example.notespasswords.ui.password;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PasswordViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PasswordViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is password fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}