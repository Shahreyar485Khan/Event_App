package com.example.eventapp.Utils;

import android.widget.EditText;

public class TextUtils {


    public static boolean isNotEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return true;

        return false;
    }

}
