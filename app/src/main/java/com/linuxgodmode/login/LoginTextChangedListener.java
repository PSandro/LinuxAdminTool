package com.linuxgodmode.login;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.common.base.Preconditions;
import com.linuxgodmode.MainActivity;

/**
 * Created by sandro on 15.04.17 for project androidapp.
 */

public class LoginTextChangedListener implements TextWatcher {


    private final MainActivity mainActivity;

    public LoginTextChangedListener(MainActivity mainActivity) {
        Preconditions.checkNotNull(mainActivity, "The mainActivity cannot be null");
        this.mainActivity = mainActivity;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean valid = this.mainActivity.checkInputVadility();
        mainActivity.runOnUiThread(() -> {
            this.mainActivity.connectButton.setEnabled(valid);
        });

    }
}
