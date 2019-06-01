package com.example.root.teachersusiev0;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChooseTopicActivity extends AppCompatActivity {

    // GUI
    Spinner spinner_chooseTopic;
    Button button_ok;
    Switch switch_ansToQues;

    // database
    DBHelper dbHelper;

    // global var
    GlobalVars globalVars;
    AlertDialog.Builder dialogBox;

    // other vars
    List<String> unique_topics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_topic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // adding back button to MainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // globalVars
        globalVars = (GlobalVars) getApplication();

        // database var
        dbHelper = new DBHelper(getApplicationContext(), globalVars.getDatabaseName(), null, globalVars.getDatabaseVersion());

        // AlertDialog Box
        dialogBox = new AlertDialog.Builder(this);
        dialogBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
            }
        });

        // list to get unique topics
        unique_topics = new ArrayList<>();

        // spinner_chooseTopic
        spinner_chooseTopic = (Spinner) findViewById(R.id.chooseTopicActivity_spinner_chooseTopic);

        // get unique values of topics
        unique_topics = dbHelper.getUniqueTopics();
        // adding unique topics to spinner_topic
        ArrayAdapter<String> spinner_topicAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinner_topicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_chooseTopic.setAdapter(spinner_topicAdapter);
        spinner_topicAdapter.add("SELECT TOPIC");
        for(int i=0; i<unique_topics.size(); i++) {
            spinner_topicAdapter.add(unique_topics.get(i));
        }
        spinner_topicAdapter.notifyDataSetChanged();

        // switch
        switch_ansToQues = (Switch) findViewById(R.id.chooseTopicActivity_switch_ansToQues);

        // button_ok
        button_ok = (Button) findViewById(R.id.chooseTopicActivity_button_Ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedTopic = spinner_chooseTopic.getSelectedItem().toString();
                if(selectedTopic.equals("SELECT TOPIC")) {
                    dialogBox.setTitle("ERROR");
                    dialogBox.setMessage("Please a select valid topic");
                    dialogBox.create().show();
                } else {
                    globalVars.setTopic(selectedTopic);
                    globalVars.setIsAnswerToQuestion(switch_ansToQues.isChecked());
                    Intent intent = new Intent(ChooseTopicActivity.this, TestActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

}
