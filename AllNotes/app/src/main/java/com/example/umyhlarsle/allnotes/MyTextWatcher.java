package com.example.umyhlarsle.allnotes;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

/**
 * Created by umyhlarsle on 2015-12-10.
 */
public class MyTextWatcher implements TextWatcher {

    SecondActivity context;

    public MyTextWatcher(SecondActivity context){

        this.context = context;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {

        if (s.length()>25){
            CharSequence tmp =s.subSequence(0,25);
            context.editTitle.setText(tmp);
            context.editTitle.setSelection(tmp.length());
            Toast.makeText(context, "The title cannot exceed 30 characters",
                    Toast.LENGTH_LONG).show();
        }
    }
}
