package com.example.jatal.todolist;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {
    protected ListView mListView;

    protected NotesBDD myBDD;

    protected ArrayList<Note> arrayOfNotes;
    protected NotesAdapter adapter;

    protected int order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        mListView = (ListView) findViewById(R.id.list_note);
        myBDD = new NotesBDD(this);
        ReloadList();

        order = -1;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                intent.putExtra("EXTRA_NOTE", 0);
                startActivityForResult(intent, 0);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, ViewNoteActivity.class);
                intent.putExtras(arrayOfNotes.get(position).getNValue());
                startActivityForResult(intent, 0);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                myBDD.removeNoteWithID(arrayOfNotes.get(position).getId());
                                showMsg("Item removed");
                                ReloadList();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want to delete this note?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            }
        });
    }

    protected void ReloadList()
    {
       Note[] tmp;
       arrayOfNotes = new ArrayList<Note>();
        adapter = new NotesAdapter(getApplicationContext(), arrayOfNotes);
        myBDD.open();
        if (order > 0)
            tmp = myBDD.getNote(order);
        else
            tmp = myBDD.getNote();
        if (tmp != null) {
            adapter.addAll(tmp);
            mListView.setAdapter(adapter);
        }
//        myBDD.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showMsg("Coming soon");
            return true;
        }
        else if (id == R.id.reorder) {
            return true;
        }
        else if (id == R.id.obcreat) {
            if (order == 1) {
                showMsg("Ordered by creation date desc");
                order = 3;
            }
            else {
                showMsg("Ordered by creation date asc");
                order = 1;
            }
            ReloadList();
            return true;
        }
        else if (id == R.id.obname) {
            if (order == 2) {
                showMsg("Ordered by name desc");
                order = 4;
            }
            else {
                showMsg("Ordered by name asc");
                order = 2;
            }
            ReloadList();
            return true;
        }
        else if (id == R.id.obtodo) {
            if (order == 5) {
                showMsg("Ordered by ToDo desc");
                order = 6;
            }
            else {
                showMsg("Ordered by ToDo asc");
                order = 5;
            }
            ReloadList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMsg(String message) {
        Toast msg = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
        msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
                msg.getYOffset() / 2);
        msg.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Note new_Note;
        if (resultCode > 0) {
            Bundle extras = data.getExtras();
            if (resultCode == 2 || resultCode == 4 || (resultCode == 10 && extras.getInt("EXTRA_REMIND") == 0)) {
                new_Note = new Note(extras.getString("EXTRA_NAME"), extras.getString("EXTRA_CONTENT"), extras.getInt("EXTRA_ID"), extras.getInt("EXTRA_TODO"));
            }
            else if (resultCode == 1 || resultCode == 3 || resultCode == 10) {
                new_Note = new Note(extras.getString("EXTRA_NAME"), extras.getString("EXTRA_CONTENT"),
                        extras.getInt("EXTRA_DAY"), extras.getInt("EXTRA_MONTH"),
                        extras.getInt("EXTRA_YEAR"), extras.getInt("EXTRA_HOUR"),
                        extras.getInt("EXTRA_MIN"), extras.getInt("EXTRA_ID"), extras.getInt("EXTRA_TODO"));
                SetAlarm(new_Note);
            }
            else
                new_Note = new Note();

            if (resultCode == 10) {
                new_Note.addState(extras.getInt("EXTRA_UPSTATE"));
            }


            myBDD.open();
            if (resultCode == 1 || resultCode == 2) {
                myBDD.insertNote(new_Note);
            }
            else if (resultCode == 3 || resultCode == 4 || resultCode == 10) {
                myBDD.updateNote(extras.getInt("EXTRA_ID"), new_Note);
            }
            myBDD.close();
        }
        ReloadList();
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void SetAlarm(Note note) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(note.getYear(), note.getMonth(), note.getDay(), note.getHour(), note.getMin());
        long delay = (calendar.getTimeInMillis()-System.currentTimeMillis())/1000;
        if (delay > 0)
            scheduleNotification(getNotification("Un travail important vous attends!",note.getName()), 20000);
    }

    private void scheduleNotification(Notification notification, long delay) {

        Intent notificationIntent = new Intent(this, NotifReceiver.class);
        notificationIntent.putExtra(NotifReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotifReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String Title, String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(Title);
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_notif);
        return builder.build();
    }
}
