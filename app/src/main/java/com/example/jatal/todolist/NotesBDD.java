package com.example.jatal.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Collection;

public class NotesBDD {
    private static final int VERSION_BDD = 7;
    private static final String NOM_BDD = "notes.db";

    private static final String TABLE_NOTES = "table_notes";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_CONTENT = "CONTENT";
    private static final int NUM_COL_CONTENT = 1;
    private static final String COL_NAME = "NAME";
    private static final int NUM_COL_NAME = 2;
    private static final String COL_DAY = "DAY";
    private static final int NUM_COL_DAY = 3;
    private static final String COL_MONTH = "MONTH";
    private static final int NUM_COL_MONTH = 4;
    private static final String COL_YEAR = "YEAR";
    private static final int NUM_COL_YEAR = 5;
    private static final String COL_HOUR = "HOUR";
    private static final int NUM_COL_HOUR = 6;
    private static final String COL_MIN = "MIN";
    private static final int NUM_COL_MIN = 7;
    private static final String COL_TODO = "TODO";
    private static final int NUM_COL_TODO = 8;

    private myBaseSQLite maBaseSQLite;
    private SQLiteDatabase bdd;

    public NotesBDD(Context context){
        maBaseSQLite = new myBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    private ContentValues returnValue(Note note){
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, note.getContent());
        values.put(COL_NAME, note.getName());
        if (note.getRemind() == true) {
            values.put(COL_DAY, note.getDay());
            values.put(COL_MONTH, note.getMonth());
            values.put(COL_YEAR, note.getYear());
            values.put(COL_HOUR, note.getHour());
            values.put(COL_MIN, note.getMin());
        }
        else {
            values.put(COL_DAY, 0);
            values.put(COL_MONTH, 0);
            values.put(COL_YEAR, 0);
            values.put(COL_HOUR, 0);
            values.put(COL_MIN, 0);
        }
        values.put(COL_TODO, note.getState());
        return values;
    }

    public long insertNote(Note note){
        return bdd.insert(TABLE_NOTES, null, returnValue(note));
    }

    public int updateNote(int id, Note note){
        return bdd.update(TABLE_NOTES, returnValue(note), COL_ID + " = " +id, null);
    }

    public int removeNoteWithID(int id){
        return bdd.delete(TABLE_NOTES, COL_ID + " = " +id, null);
    }

    public Note[] getNote() {
        Cursor c = bdd.query(TABLE_NOTES, null, null, null, null, null, null);
        return cursorToNote(c);
    }

    public Note[] getNote(int i) {
        Cursor c;
        if (i == 1)
            c = bdd.query(TABLE_NOTES, null, null, null, null, null, COL_ID + " ASC");
        else if (i == 2)
            c = bdd.query(TABLE_NOTES, null, null, null, null, null, COL_NAME + " ASC");
        else if (i == 3)
            c = bdd.query(TABLE_NOTES, null, null, null, null, null, COL_ID + " DESC");
        else if (i == 4)
            c = bdd.query(TABLE_NOTES, null, null, null, null, null, COL_NAME + " DESC");
        else if (i == 5)
            c = bdd.query(TABLE_NOTES, null, null, null, null, null, COL_TODO + " ASC");
        else
            c = bdd.query(TABLE_NOTES, null, null, null, null, null, COL_TODO + " DESC");
        return cursorToNote(c);
    }

    private Note[] cursorToNote(Cursor c){
        if (c.getCount() <= 0)
            return null;
        c.moveToFirst();
        int i = c.getCount();
        int x = 0;
        Note[] my_col = new Note[i];
        Note note;

        while (x < i) {
            note = new Note();
            note.setId(c.getInt(NUM_COL_ID));
            note.setName(c.getString(NUM_COL_NAME));
            note.setContent(c.getString(NUM_COL_CONTENT));
            note.setState(c.getInt(NUM_COL_TODO));
            if (c.getInt(NUM_COL_DAY) != 0) {
                note.setRemind(true);
                note.setDay(c.getInt(NUM_COL_DAY));
                note.setMonth(c.getInt(NUM_COL_MONTH));
                note.setYear(c.getInt(NUM_COL_YEAR));
                note.setMin(c.getInt(NUM_COL_MIN));
                note.setHour(c.getInt(NUM_COL_HOUR));
            }
            if (note.getName() != null)
                my_col[x] = note;
            x++;
            c.moveToNext();
        }
        c.close();
        return my_col;
    }

}