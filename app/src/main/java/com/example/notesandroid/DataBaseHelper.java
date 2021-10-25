package com.example.notesandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Mohamad Jamous on 12/10/2021
 */

public class DataBaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "NOTES_DATABASE";

    private static final String TABLE_NAME = "notes_table";

    private static final String NOTE_TITLE = "note_title";
    private static final String NOTE_CONTENT = "note_content";
    private static final String NOTE_TIME = "note_time";
    private static final String NOTE_DATE = "note_date";
    private static final String NOTE_ID = "note_Id";



    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String tableString = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOTE_TITLE + " TEXT, " + NOTE_TIME + " TEXT, " + NOTE_DATE + " TEXT, " + NOTE_CONTENT + " TEXT, " + NOTE_ID + " INTEGER)";
        sqLiteDatabase.execSQL(tableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public boolean InsertNote(NoteObject note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(NOTE_TITLE, note.getTitle());
        contentValues.put(NOTE_TIME, note.getTime());
        contentValues.put(NOTE_CONTENT, note.getContent());
        contentValues.put(NOTE_DATE, note.getDate());
        contentValues.put(NOTE_ID, note.getId());


        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        //if date is inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor FetchNoteTitle(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + NOTE_TITLE + " FROM " + TABLE_NAME, null);
        return data;
    }


    public Cursor FetchNoteContent(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + NOTE_CONTENT + " FROM " + TABLE_NAME, null);
        return data;
    }

    public Cursor FetchNoteTime(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + NOTE_TIME + " FROM " + TABLE_NAME, null);
        return data;
    }

    public Cursor FetchNoteDate(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + NOTE_DATE + " FROM " + TABLE_NAME, null);
        return data;
    }

    public Cursor FetchNoteId(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + NOTE_ID + " FROM " + TABLE_NAME, null);
        return data;
    }


    public void DeleteNote(NoteObject note){

        SQLiteDatabase db = this.getWritableDatabase();

        String query =
                NOTE_TITLE + "='" + note.getTitle() +
                "' AND " + NOTE_CONTENT + "='" + note.getContent() +
                "' AND " + NOTE_TIME + "='" + note.getTime() +
                "' AND " + NOTE_DATE + "='" + note.getDate() +
                "' AND " + NOTE_ID + "='" + note.getId() + "'";

        System.out.println("queryValue: " + query);

        db.delete(TABLE_NAME, query, null);

        db.close();

    }

    public void ClearDataBase(){
        SQLiteDatabase db = this.getWritableDatabase();

        //delete all rows in a table
        db.execSQL("DELETE FROM notes_table");
        db.close();
    }


    public boolean UpdateNote(NoteObject note)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NOTE_TITLE, note.getTitle());
        values.put(NOTE_CONTENT, note.getContent());
        values.put(NOTE_TIME, note.getTime());
        values.put(NOTE_DATE, note.getDate());
        values.put(NOTE_ID, note.getId());

        String whereClause = NOTE_ID + "=?";
        String whereArgs[] = {String.valueOf(note.getId())};

        int result = db.update(TABLE_NAME, values, whereClause, whereArgs);
        System.out.println("updatedRows: " + result);

        //returns number of rows affected.
        if (result == 0) {
            return false;
        } else {
            return true;
        }
    }


}
