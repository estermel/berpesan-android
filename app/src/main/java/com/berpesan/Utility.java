package com.berpesan;


import android.widget.EditText;

/**
 * Created by itdel on 4/3/17.
 */

public class Utility {
    public static boolean isBlankField(EditText etPersonData)
    {
        return etPersonData.getText().toString().trim().equals("");
    }
}
