package com.hit.todolistapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is the database handler will save all the data the user enters for the tasks he needs to perform.
 * The task name and the deadline date to this task.
 * All user tasks will be shown in a list that they can add/update/delete each time they log into the app.
 * All the data will be stores in app using SQLite.
 * Implement the MVVM architecture
 * @author Alex Mordasov
 * @version 1.0
 * @since 2020-03-19
 */

public class ModelSqlLiteDatabase extends SQLiteOpenHelper implements Imodel {
    // create SQLite database
    private SQLiteDatabase db;
    private Context context;
    private static final String DATABASE_NAME = "Todo.db";
    private static final String TABLE_ITEMS = "to_do_items";
    private static final int DATABASE_VERSION = 3;
    private static final String CREATE_TABLE_TODO = "CREATE TABLE " + TABLE_ITEMS +
            "(" + Columns.I_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Columns.I_COL_2 + " TEXT," +
            Columns.I_COL_3 + " TEXT," +
            Columns.I_COL_4 + " INTEGER," +
            Columns.I_COL_5 + " TEXT)";


    abstract class Columns {
        // all the database columns
        private static final String I_COL_1 = "serialnum";
        private static final String I_COL_2 = "date";
        private static final String I_COL_3 = "name";
        private static final String I_COL_4 = "userid";
        private static final String I_COL_5 = "time";
    }

    /***
     * Constructor
     * @param context
     */
    ModelSqlLiteDatabase(Context context) {
        // calling SQLiteOpenHelper constructor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // set which activity is using the context
        this.context = context;

        // must be last line in the constructor,in order to make sure all variables are initialized prior to onCreate()
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create todo table
        db.execSQL(CREATE_TABLE_TODO);
        this.showToast(this.context, "initialized");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_ITEMS + "'");
        onCreate(db);
    }

    @Override
    public void showToast(Context context, String txt) { //to show toast when using "this."
        Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
    }

    @Override
    public void deleteFromToDoList(String date,String time, String name) {
        //task to be removed from the database
        String whereClause = "date=? AND time =? AND name =?";
        String[] whereArgs = new String[]{date,time, name};
        //delete the task and show toast
        if (db.delete(TABLE_ITEMS, whereClause, whereArgs) == 0) {
            Toast.makeText(context, "Error: activity does not appear in the database", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "The task deleted successfully", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void updateToDoList(String date,String time,String name, String updateDate,String updateTime, String updateName) {
        ContentValues values = new ContentValues();
        String whereClause = "date=? AND time =? AND name =?";
        String[] whereArgs = new String[]{date,time, name};

        //fill in the values
        values.put(Columns.I_COL_2, updateDate);
        values.put(Columns.I_COL_3, updateName);
        values.put(Columns.I_COL_5, updateTime);
        if (db.update(TABLE_ITEMS, values, whereClause, whereArgs) == 0) {
            Toast.makeText(context, "Error: activity does not appear in the db", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "The task updated successfully", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void addItem(int userId, String date,String time, String name) {

        //initialize ContentValues object
        ContentValues values = new ContentValues();

        values.put(Columns.I_COL_2, date);
        values.put(Columns.I_COL_3, name);
        values.put(Columns.I_COL_4, userId);
        values.put(Columns.I_COL_5, time);

        //make the insertion to the database
        if (db.insert(TABLE_ITEMS, null, values) != -1) {
            this.showToast(this.context, "Task as been added to list");
        } else {
            this.showToast(this.context, "Error: try again to add");
        }
    }

    @Override
    public String getItems() throws JSONException { //load todo from database
        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();

        json.put("toDoItems", arr);

        String query = "SELECT * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
                JSONObject item = new JSONObject();
                item.put("serial", cursor.getString(cursor.getColumnIndex("serialnum")));
                item.put("id", cursor.getString(cursor.getColumnIndex("userid")));
                item.put("item", cursor.getString(cursor.getColumnIndex("name")));
                item.put("date", cursor.getString(cursor.getColumnIndex("date")));
                item.put("time", cursor.getString(cursor.getColumnIndex("time")));
                arr.put(item);
            }
        }
        //must close the cursor at the end
        return json.toString();
    }
}