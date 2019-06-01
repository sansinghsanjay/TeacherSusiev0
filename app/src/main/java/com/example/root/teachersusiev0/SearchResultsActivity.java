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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    // GUI
    ListView listView;

    // global variables
    GlobalVars globalVars;
    AlertDialog.Builder dialogBox;

    // database vars
    DBHelper dbHelper;

    // other variables
    String searchQuery;
    List<Integer> idList;
    List<String> topicList;
    List<String> questionList;
    List<String> answerList;
    List<Integer> hardList;
    int[] idArray;
    String[] topicArray;
    String[] questionArray;
    String[] answerArray;
    int[] hardArray;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // back button to parentActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // global variables
        globalVars = (GlobalVars) getApplication();

        // init dbHelper
        dbHelper = new DBHelper(getApplicationContext(), globalVars.getDatabaseName(), null, globalVars.getDatabaseVersion());

        // other vars declaration
        idList = new ArrayList<>();
        topicList = new ArrayList<>();
        questionList = new ArrayList<>();
        answerList = new ArrayList<>();
        hardList = new ArrayList<>();

        // listView
        listView = (ListView) findViewById(R.id.searchResult_listView);
        context = listView.getContext();

        // get searchQuery
        searchQuery = globalVars.getSearchQuery();
        globalVars.setSearchQuery("");

        // reset listView
        listView.setAdapter(null);
        if(searchQuery.length() <= 0) {
            dialogBox.setTitle("Error");
            dialogBox.setMessage("Enter search query");
            dialogBox.create().show();
        } else {
            // get data
            List<List<String>> resultRows = dbHelper.getSearchData(searchQuery);
            // iterate resultData to show searched result in ListView
            for (int i = 0; i < resultRows.size(); i++) {
                List<String> resultRow;
                resultRow = resultRows.get(i);
                // extract topic, question, answer, and hard, and save it in their respective list
                idList.add(Integer.parseInt(resultRow.get(0)));
                topicList.add(resultRow.get(1));
                questionList.add(resultRow.get(2));
                answerList.add(resultRow.get(3));
                hardList.add(Integer.parseInt(resultRow.get(4)));
                // clear resultRow
                resultRow = null;
            }
            // converting topicList into topicArray
            idArray = new int[idList.size()];
            for (int i = 0; i < idList.size(); i++) {
                idArray[i] = idList.get(i);
            }
            // converting topicList into topicArray
            topicArray = new String[topicList.size()];
            for (int i = 0; i < topicList.size(); i++) {
                topicArray[i] = topicList.get(i);
            }
            // converting questionList into questionArray
            questionArray = new String[questionList.size()];
            for (int i = 0; i < questionList.size(); i++) {
                questionArray[i] = idList.get(i) + ". " + questionList.get(i);
            }
            // converting answerList into answerArray
            answerArray = new String[answerList.size()];
            for (int i = 0; i < answerList.size(); i++) {
                answerArray[i] = answerList.get(i);
            }
            // converting hardList into hardArray
            hardArray = new int[hardList.size()];
            for (int i = 0; i < hardList.size(); i++) {
                hardArray[i] = hardList.get(i);
            }
            // clearing all lists
            idList.clear();
            topicList.clear();
            questionList.clear();
            answerList.clear();
            hardList.clear();
            // create adapter
            //Toast.makeText(getApplicationContext(), questionArray[0], Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), answerArray[0], Toast.LENGTH_SHORT).show();
            MyListAdapter adapter = new MyListAdapter(SearchResultsActivity.this, questionArray, answerArray);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                globalVars.setSearchResultID(idArray[position]);
                Intent intent = new Intent(SearchResultsActivity.this, SearchTestActivity.class);
                startActivity(intent);
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
