package com.example.jatal.todolist;

import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class myBaseSQLite extends SQLiteOpenHelper {
    private static final String TABLE_NOTES = "table_notes";
    private static final String COL_ID = "ID";
    private static final String COL_CONTENT = "CONTENT";
    private static final String COL_NAME = "NAME";
    private static final String COL_DAY = "DAY";
    private static final String COL_MONTH = "MONTH";
    private static final String COL_YEAR = "YEAR";
    private static final String COL_HOUR = "HOUR";
    private static final String COL_MIN = "MIN";
    private static final String COL_TODO = "TODO";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_NOTES + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_CONTENT + " TEXT NOT NULL, "
            + COL_NAME + " TEXT NOT NULL, "+ COL_DAY + " INTEGER DEFAULT 0, "
            + COL_MONTH + " INTEGER DEFAULT 0, " + COL_YEAR + " INTEGER DEFAULT 0, "
            + COL_HOUR + " INTEGER DEFAULT 0, " + COL_MIN + " INTEGER DEFAULT 0, "
            + COL_TODO + " INTEGER DEFAULT -1);";

    public myBaseSQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES + ";");
        onCreate(db);
    }

}