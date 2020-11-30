package com.example.davonlineshop.ui.shoppingcart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShoppingCartViewModel  extends ViewModel {

    private MutableLiveData<String> mText;

    public ShoppingCartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Cart");
    }

    public LiveData<String> getText() {
        return mText;
    }
}