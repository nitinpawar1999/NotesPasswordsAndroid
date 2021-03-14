package com.example.notespasswords.ui.note;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class noteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public noteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is note fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}