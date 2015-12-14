package com.example.umyhlarsle.allnotes;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToDoNoteFragment extends Fragment {

    RelativeLayout relativeLayout;
    ScrollView scrollView;
    LinearLayout linLayout;
    CheckBox checkBox;
    int checkBoxId =0;


    public ToDoNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_do_note, container, false);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.relativelayout2);
        scrollView = (ScrollView)relativeLayout.findViewById(R.id.scroll_view);
        linLayout = (LinearLayout)scrollView.findViewById(R.id.custom_inflated_list_layout);


    ///for-loop för varje item i todonote
        for (int i =0;i<10;i++) {

            View inflatedView = inflater.inflate(R.layout.inflated_layout, linLayout, false);
            checkBox = (CheckBox)inflatedView.findViewById(R.id.check_box);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ///Kollar bara den sista förekomsten, setTag eller id på checkbox.

                       if (checkBox.isChecked()) {
                           Log.i("TAG", "Checked");
                       } else {
                           Log.i("TAG", "Not Checked");
                       }


                }
            });


            linLayout.addView(inflatedView);
        }


        return view;
    }


}
