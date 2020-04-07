package com.hit.todolistapplication;


public interface IViewModel {

    void getItems() ;
    void addItem(String date,String time ,String name);
    void deleteItem(String date,String time, String name);
    void updated(String date,String time, String name,String updateDate,String updateTime, String updateName);
}
