package com.example.jatal.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ViewNoteActivity extends AppCompatActivity {
    private TextView name;
    private TextView date;
    private TextView content;
    private ImageView state;
    private int stateV;
    private int originalS;

    void ReloadState() {
        if (stateV == -1)
            state.setImageResource(R.drawable.ic_todo);
        else if (stateV == 0)
            state.setImageResource(R.drawable.ic_doing);
        else if (stateV == 1) {
            state.setImageResource(R.drawable.ic_done);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        state = (ImageView) findViewById(R.id.todo);
        name = (TextView) findViewById(R.id.nameView);
        date = (TextView) findViewById(R.id.dateView);
        content = (TextView) findViewById(R.id.contentView);
        Bundle b = getIntent().getExtras();
        stateV = b.getInt("EXTRA_TODO");
        originalS = stateV;
        ReloadState();
        name.setText(b.getString("EXTRA_NAME"));
        content.setText(b.getString("EXTRA_CONTENT"));
        if (b.getInt("EXTRA_REMIND") == 1)
            date.setText(b.getInt("EXTRA_DAY") + "/" + b.getInt("EXTRA_MONTH"));
        else {
            findViewById(R.id.Spacerview).setVisibility(View.INVISIBLE);
            date.setVisibility(View.INVISIBLE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewNoteActivity.this, NewNoteActivity.class);
                intent.putExtras(getIntent().getExtras());
                startActivityForResult(intent, 0);
            }
        });

        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.fabSave);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent return_int = new Intent();
                int count = 0;
                while (originalS != stateV)
                {
                    originalS++;
                    if (originalS >= 2)
                        originalS = -1;
                    count++;
                }
                if (count > 0) {
                    return_int.putExtras(getIntent().getExtras());
                    return_int.putExtra("EXTRA_UPSTATE", count);
                    ViewNoteActivity.this.setResult(10, return_int);
                }
                else {
                    ViewNoteActivity.this.setResult(0, return_int);
                }
                ViewNoteActivity.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode > 0) {
            Bundle extras = data.getExtras();
            Intent return_int = new Intent();
            return_int.putExtras(extras);
            if (extras.getInt("EXTRA_REMIND") == 1)
                ViewNoteActivity.this.setResult(3, return_int);
            else
                ViewNoteActivity.this.setResult(4, return_int);
            ViewNoteActivity.this.finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void ReSetState(View view) {
        stateV++;
        if (stateV >= 2) {
            stateV = -1;
        }
        ReloadState();
    }
}
