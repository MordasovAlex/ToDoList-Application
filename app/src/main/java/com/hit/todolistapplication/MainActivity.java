package com.hit.todolistapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private IViewModel viewModel;
    private Imodel model;


    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //creating the webView object
        webView = new WebView(this);
        setContentView(webView);
        //creating the model object
        model = new ModelSqlLiteDatabase(this);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().getAllowUniversalAccessFromFileURLs();
        WebView.setWebContentsDebuggingEnabled(true);
        webView.loadUrl("file:///android_asset/UserInterface.html");
        webView.getSettings().setDomStorageEnabled(true);

        //creating ViewModel object
        viewModel = new ViewModel(model, webView);
        //attaching  ViewModel object to the WebView
        webView.addJavascriptInterface(viewModel, "vm");

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) { //first check if can go back if yes then go back
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
