package com.hit.todolistapplication;

import android.content.Context;

import org.json.JSONException;

public interface Imodel {
    void showToast(Context context, String txt);//to show toast when using "this."
    void deleteFromToDoList(String date,String time, String name);//tasks to be removed from the database
    void updateToDoList(String date,String time, String name, String updateDate,String updateTime, String updateName);
    void addItem(int userId, String date,String time, String name);//initialize ContentValues object
    String getItems() throws JSONException; //load tasks from DB
}
