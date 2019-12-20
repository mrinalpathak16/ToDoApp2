package com.example.todoapp2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialog extends AppCompatDialogFragment {
    private EditText label, desc;
    private RadioButton normal, priority;
    private TextView normalText, priorityText;
    private CheckBox checkBox;
    private TimePicker time;
    private DatePicker date;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        String title = "Task Dialog";
        //TODO: put Uri here if(item==null){
        //    title = "Add Task";
        //}
        //else{
        //    title = "Edit Task";
        //}

        builder.setView(view)
                .setTitle(title)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int type;
                        if (normal.isChecked()) {
                            type = 0;
                        } else {
                            type = 1;
                        }
                        String lab = label.getText().toString();
                        String des;
                        if (checkBox.isChecked()) {
                            des = desc.getText().toString();
                        } else {
                            des = "";
                        }
                        String t = "" + time.getHour() + ":" + time.getMinute();
                        String d = "" + date.getDayOfMonth() + "/" + (date.getMonth() + 1) + "/" + date.getYear();
                    }
                });
        normal = view.findViewById(R.id.normalTask);
        priority = view.findViewById(R.id.priorityTask);
        normalText = view.findViewById(R.id.normalTaskText);
        priorityText = view.findViewById(R.id.priorityTaskText);
        normalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normal.toggle();
            }
        });
        priorityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priority.toggle();
            }
        });
        label = view.findViewById(R.id.label);
        checkBox = view.findViewById(R.id.checkBox);
        desc = view.findViewById(R.id.desc);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked())
                    desc.setVisibility(View.VISIBLE);
                else {
                    desc.setVisibility(View.GONE);
                    desc.setText("");
                }
            }
        });
        time = view.findViewById(R.id.time);
        time.setIs24HourView(true);
        date = view.findViewById(R.id.date);
        date.setMinDate(System.currentTimeMillis());

        normal.setChecked(true);
        label.setText("Label 1");
        checkBox.setChecked(true);
        desc.setText("This is the description of Task 1");
        desc.setVisibility(View.VISIBLE);

        return builder.create();
    }

}
