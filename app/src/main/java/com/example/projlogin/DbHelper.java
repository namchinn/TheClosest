package com.example.projlogin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    Context context;

    public static final String DBNAME = "Login.db";
    private static final String TableName = "mynotes";
    private static final String ColumnId = "id";
    private static final String ColumnTitle = "title";
    private static final String ColumnDescription = "description";


    public static final String EVENT_TABLE_NAME = "eventstable" ;
    public static final String EVENT = "event" ;
    public static final String TIME = "time" ;
    public static final String DATE = "date" ;
    public static final String MONTH = "month" ;
    public static final String YEAR = "year" ;



    public DbHelper(Context context) {
        super(context, "Login.db", null, 3);

    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {

        MyDB.execSQL("create table users(username TEXT primary key, password TEXT)");
        MyDB.execSQL("create table eventstable(id INTEGER primary key autoincrement, event TEXT, time TEXT, date TEXT, month TEXT, year TEXT)");


       /* String query = "CREATE TABLE " + TableName +
                " (" + ColumnId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ColumnTitle + " TEXT, " +
                ColumnDescription + " TEXT);";
        MyDB.execSQL(query);


        /*String query = "CREATE TABLE " + "myNotes" + "(" + ColumnId + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ColumnTitle + "TEXT"+
                ColumnDescription + "TEXT);";*/
       MyDB.execSQL("create table mynotes(id INTEGER primary key autoincrement, title TEXT, description TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop table if exists users");
        MyDB.execSQL("drop table if exists mynotes");
        MyDB.execSQL("drop table if exists eventstable");
        onCreate(MyDB);

    }


    public void SaveEvent(String event, String time, String date, String month, String year, SQLiteDatabase database)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT, event);
        contentValues.put(TIME, time);
        contentValues.put(DATE, date);
        contentValues.put(MONTH, month);
        contentValues.put(YEAR, year);
        database.insert(EVENT_TABLE_NAME, null, contentValues);
    }

    public Cursor ReadEvents(String date, SQLiteDatabase database)
    {
        String[] Projections = {EVENT, TIME, DATE, MONTH, YEAR};
        String Selection = DATE + "=?";
        String[] SelectionArga = {date};
        return database.query(EVENT_TABLE_NAME, Projections, Selection, SelectionArga, null, null, null,null);
    }
    public Cursor ReadEventsperMonth(String month, String year, SQLiteDatabase database)
    {
        String[] Projections = {EVENT, TIME, DATE, MONTH, YEAR};
        String Selection = DATE + "=? and " + YEAR + "=?";
        String[] SelectionArga = {month, year};
        return database.query(EVENT_TABLE_NAME, Projections, Selection, SelectionArga, null, null, null,null);
    }



    void addNotes(String title, String description){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(ColumnTitle, title);
        cv.put(ColumnDescription, description);

        long resultValue = db.insert(TableName,null,cv);

        if(resultValue == -1){
            Toast.makeText(context,"Өгөгдөл нэмэгдсэнгүй", Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean insertData(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users",null,contentValues);
        if(result == -1) return false;
        else  return true;
    }

    public Boolean checkusername(String username){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where username = ?",new String[] {username});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username, password});

        if(cursor.getCount()>0) return true;
        else
            return false;
    }









    Cursor readAllData()
    {
       //String query = "SELECT * FROM " + TableName;
        // String query = "Select * from mynotes";
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;

        if(database != null)
        {
            cursor = database.rawQuery("Select * from mynotes ", null);
        }
        return cursor;
    }

    void deleteAllNotes()
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "Delete from " + TableName;
        database.execSQL(query);

    }

    void updateNotes(String title, String description, String id)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(ColumnTitle, title);
        cv.put(ColumnDescription, description);

        long result = database.update(TableName, cv,"id=?", new String[]{id});

        if(result == -1)
        {
            Toast.makeText(context, "Амжилтгүй боллоо", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteSingleItem(String id)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        long result = database.delete(TableName,"id=?", new String[]{id});

        if(result == -1)
        {
            Toast.makeText(context, "Тэмдэглэл устгагдсангүй", Toast.LENGTH_SHORT).show();
        }

    }
}
