package com.example.todoapp2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class DetailsDialog extends AppCompatDialogFragment {
    private TextView typeText, labelText, descText, timeText, dateText;
    private Context mContext;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.details_dialog, null);

        builder.setView(view)
                .setTitle("Task Details")
                .setNegativeButton("edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OngoingFragment.EditDialogListener listener =
                                (OngoingFragment.EditDialogListener)mContext;
                        listener.openDialog();
                    }
                })
                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        typeText = view.findViewById(R.id.typeText);
        typeText.setText("Normal Task");

        labelText = view.findViewById(R.id.labelText);
        labelText.setText("Label 1");

        descText = view.findViewById(R.id.descText);
        String d = "This is the description of Task 1";
        if(d.equals("")){
            LinearLayout descRoot = view.findViewById(R.id.descRoot);
            descRoot.setVisibility(View.GONE);
        }
        else{
            descText.setText(d);
        }

        timeText = view.findViewById(R.id.timeText);
        timeText.setText("22:12");

        dateText = view.findViewById(R.id.dateText);
        dateText.setText("27/11/2019");

        return builder.create();
    }

    public void setVal(Context context){
        this.mContext = context;
    }
}
