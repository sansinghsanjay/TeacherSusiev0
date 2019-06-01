package com.example.root.teachersusiev0;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    // GUI
    EditText editText_searchQuery;
    Button button_search;

    // global variables
    GlobalVars globalVars;
    AlertDialog.Builder dialogBox;

    // database vars
    DBHelper dbHelper;

    // other variables
    String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // back button to parentActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // global variables
        globalVars = (GlobalVars) getApplication();

        // init dbHelper
        dbHelper = new DBHelper(getApplicationContext(), globalVars.getDatabaseName(), null, globalVars.getDatabaseVersion());

        // editText_searchQuery
        editText_searchQuery = (EditText) findViewById(R.id.searchActivity_editText_searchQuery);

        // button_search
        button_search = (Button) findViewById(R.id.searchActivity_button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get search query
                searchQuery = editText_searchQuery.getText().toString();
                if(searchQuery.length() <= 0) {
                    dialogBox.setTitle("Error");
                    dialogBox.setMessage("Please enter a valid search query.");
                    dialogBox.create().show();
                } else {
                    globalVars.setSearchQuery(searchQuery);
                    Intent intent = new Intent(SearchActivity.this, SearchResultsActivity.class);
                    startActivity(intent);
                }
            }
        });

        // AlertDialog Box
        dialogBox = new AlertDialog.Builder(this);
        dialogBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
            }
        });
    }

}
