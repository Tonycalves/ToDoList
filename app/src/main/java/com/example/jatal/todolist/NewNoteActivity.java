package com.example.jatal.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

public final class NewNoteActivity extends AppCompatActivity {
    EditText name;
    EditText content;
    NumberPicker day;
    NumberPicker month;
    NumberPicker year;
    NumberPicker hour;
    NumberPicker min;
    Calendar clock;
    Switch mySwitch;
    int id = -1;
    int state = -1;

    private void SetHidden(boolean x)
    {
        TextView EtD;
        LinearLayout LlD;
        TextView EtT;
        LinearLayout LlT;
        EtD = (TextView) findViewById(R.id.DaterStr);
        EtT = (TextView) findViewById(R.id.TimerStr);
        LlD = (LinearLayout) findViewById(R.id.Dater);
        LlT = (LinearLayout) findViewById(R.id.Timer);
        if (x == true) {
            EtD.setVisibility(View.VISIBLE);
            EtT.setVisibility(View.VISIBLE);
            LlD.setVisibility(View.VISIBLE);
            LlT.setVisibility(View.VISIBLE);
        }
        else {
            EtD.setVisibility(View.INVISIBLE);
            EtT.setVisibility(View.INVISIBLE);
            LlD.setVisibility(View.INVISIBLE);
            LlT.setVisibility(View.INVISIBLE);
        }
    }

    private void SetPicker() {
        clock = Calendar.getInstance();
        name = (EditText) findViewById (R.id.edit_name);
        content = (EditText) findViewById (R.id.edit_content);
        day = (NumberPicker) findViewById(R.id.dayPicker);
        month = (NumberPicker) findViewById(R.id.monthPicker);
        year = (NumberPicker) findViewById(R.id.yearPicker);
        hour = (NumberPicker) findViewById(R.id.hrPicker);
        min = (NumberPicker) findViewById(R.id.minPicker);
        day.setMinValue(1);
        day.setMaxValue(31);
        day.setValue(clock.get(Calendar.DAY_OF_MONTH)+1);
        year.setMinValue(2017);
        year.setMaxValue(2100);
        year.setValue(clock.get(Calendar.YEAR));
        month.setMinValue(1);
        month.setMaxValue(12);
        month.setValue(clock.get(Calendar.MONTH)+1);
        hour.setMinValue(1);
        hour.setMaxValue(24);
        hour.setValue(12);
        min.setMinValue(0);
        min.setMaxValue(60);
    }

    private Bundle GetNValue()
    {
        Bundle extras = new Bundle();
        extras.putInt("EXTRA_ID", id);
        extras.putString("EXTRA_NAME",name.getText().toString());
        extras.putInt("EXTRA_TODO", state);
        extras.putString("EXTRA_CONTENT",content.getText().toString());
        if (mySwitch.isChecked() == true) {
            extras.putInt("EXTRA_REMIND", 1);
            extras.putInt("EXTRA_DAY", day.getValue());
            extras.putInt("EXTRA_MONTH", month.getValue());
            extras.putInt("EXTRA_YEAR", year.getValue());
            extras.putInt("EXTRA_HOUR", hour.getValue());
            extras.putInt("EXTRA_MIN", min.getValue());
        }
        else {
            extras.putInt("EXTRA_REMIND", 0);
        }
        return (extras);
    }

    private void SetNValue(Bundle v)
    {
        id = v.getInt("EXTRA_ID");
        name.setText(v.getString("EXTRA_NAME"));
        content.setText(v.getString("EXTRA_CONTENT"));
        state = v.getInt("EXTRA_TODO");
        if (v.getInt("EXTRA_REMIND") == 1) {
            mySwitch.setChecked(true);
            day.setValue(v.getInt("EXTRA_DAY"));
            month.setValue(v.getInt("EXTRA_MONTH"));;
            year.setValue(v.getInt("EXTRA_YEAR"));;
            hour.setValue(v.getInt("EXTRA_HOUR"));;
            min.setValue(v.getInt("EXTRA_MIN"));;
        }
        else {
            mySwitch.setChecked(false);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        mySwitch = (Switch) findViewById(R.id.switchRappel);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SetHidden(isChecked);
            }
        });
        SetPicker(); // Set les value du remind
        if (getIntent().getExtras().getInt("EXTRA_NOTE") == 1)
        {
            SetNValue(getIntent().getExtras());
        }
        SetHidden(mySwitch.isChecked()); // Cache la partie Remind


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmpn = name.getText().toString();
                String tmpc = content.getText().toString();
                if (tmpn.length() > 0 && tmpc.length() > 0) {
                    Intent return_int = new Intent();
                    return_int.putExtras(GetNValue());
                    if (id == -1 && mySwitch.isChecked())
                        NewNoteActivity.this.setResult(1, return_int);
                    else if (id == -1)
                        NewNoteActivity.this.setResult(2, return_int);
                    else if (mySwitch.isChecked())
                        NewNoteActivity.this.setResult(3, return_int);
                    else
                        NewNoteActivity.this.setResult(4, return_int);
                    NewNoteActivity.this.finish();
                }
            }
        });

    }
}
