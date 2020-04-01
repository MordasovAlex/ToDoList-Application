package com.hit.todolistapplication;


public interface IViewModel {

    void getItems() ;
    void addItem(String date, String name);
    void deleteItem(String date, String name);
    void updated(String date, String name, String updateDate, String updateName);
}
