package com.example.davonlineshop.ui.shoppingcart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.braintreepayments.cardform.view.CardForm;
import com.example.davonlineshop.R;

public class ShoppingCartFragment extends Fragment {

    private ShoppingCartViewModel shoppingCartViewModel;
    CardForm cardForm;
    Button ok;
    AlertDialog.Builder alertBuilder;

    public ShoppingCartFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        shoppingCartViewModel =
                ViewModelProviders.of(this).get(ShoppingCartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        cardForm = root.findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberRequired(true)
                .postalCodeRequired(true)
                .expirationRequired(true)
                .mobileNumberExplanation("Dzer heraxosi hamarin kga SMS")
                .setup((AppCompatActivity) getContext());
        ok = root.findViewById(R.id.okBtn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardForm.isValid()) {
                    alertBuilder = new AlertDialog.Builder(getContext());
                    alertBuilder.setTitle("Title")
                            .setMessage("Cart number:-> " + cardForm.getCardNumber() + "\nFull Name:-> " + cardForm.getCardholderName()
                                    + "\nCVV :-> " + cardForm.getCvv() + "\nMM:" +
                                    cardForm.getExpirationMonth() + "  YY:" +
                                    cardForm.getExpirationYear() +
                                    "\nPhone Number:-> " + cardForm.getMobileNumber())
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(), "Thank You", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            });
                        AlertDialog alertDialog = alertBuilder.create();
                        alertDialog.show();

                } else {
                    Toast.makeText(getContext(), "Invalid", Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }
}