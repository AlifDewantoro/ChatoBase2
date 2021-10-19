package com.gamatechno.chato.sdk.app.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Integer> click;

    public LiveData<Integer> initClick() {
        if (click == null) {
            click = new MutableLiveData<>();
            click.postValue(0);
        }
        return click;
    }

    void isClicked(int value) {
        if (click != null) {
            click.postValue(value);
        }
    }
}
