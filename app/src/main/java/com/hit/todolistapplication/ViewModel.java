package com.hit.todolistapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint("Registered")
class ViewModel extends Activity implements IViewModel {

    private Imodel model;
    private WebView webView;
    private ExecutorService pool;


    public ViewModel(Imodel model, WebView webView) {
        this.model = model;
        this.webView = webView;
        this.pool = Executors.newFixedThreadPool(10);
    }


    @Override
    @JavascriptInterface
    public void getItems()  { //get all the task list
        //calling getItems on model in another thread
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final String str = model.getItems();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.evaluateJavascript("showListView('" + str + "')", null);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });

    }


    @Override
    @JavascriptInterface
    public void addItem(String date,String time, String name) {
      //  add the task to database
        model.addItem(1, date,time, name);
    }


    @Override
    @JavascriptInterface
    public void deleteItem(String date,String time, String name) {//delete task from DB
        model.deleteFromToDoList(date,time, name);
    }

    @Override
    @JavascriptInterface
    public void updated(String date,String time, String name,
                        String updateDate,String updateTime ,String updateName) {//call to edit task from database function
        //TODO add filter to filter out other accounts (user_id)
        model.updateToDoList(date,time,name, updateDate,updateTime, updateName);

    }

}