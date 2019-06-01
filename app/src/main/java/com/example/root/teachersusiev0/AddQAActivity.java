package com.example.root.teachersusiev0;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class AddQAActivity extends AppCompatActivity {

    // GUI declaration
    Spinner spinner_topic;
    EditText editText_newTopic, editText_question, editText_answer;
    CheckBox isHard;
    Button button_save;
    AlertDialog.Builder dialogBox;

    // global var object
    GlobalVars globalVars;

    // other vars
    List<String> unique_topics;

    // database var
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // adding back button to MainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // AlertDialog Box
        dialogBox = new AlertDialog.Builder(this);
        dialogBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
            }
        });

        // init globalVars
        globalVars = (GlobalVars) getApplication();

        // init dbHelper
        dbHelper = new DBHelper(getApplicationContext(), globalVars.getDatabaseName(), null, globalVars.getDatabaseVersion());

        // spinner_topic
        spinner_topic = (Spinner) findViewById(R.id.addqaActivity_spinner_topic);

        // adding unique topics to spinner_topic
        setSpinnerValues();

        // editText_newTopic
        editText_newTopic = (EditText) findViewById(R.id.addqaActivity_editText_topic);

        // editText_question
        editText_question = (EditText) findViewById(R.id.addqaActivity_editText_question);

        // editText_answer
        editText_answer = (EditText) findViewById(R.id.addqaActivity_editText_answer);

        // isHard checkbox
        isHard = (CheckBox) findViewById(R.id.mainActivity_checkbox_isHard);

        // button_save
        button_save = (Button) findViewById(R.id.addqaActivity_button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = "NA", question = "NA", answer = "NA";
                int hard = -1;
                // getting topic
                if(editText_newTopic.length() > 0 && spinner_topic.getSelectedItem().toString().equals("SELECT TOPIC")) {
                    topic = editText_newTopic.getText().toString();
                } else if(editText_newTopic.length() == 0 && (spinner_topic.getSelectedItem().toString().equals("SELECT TOPIC") == false)) {
                    topic = spinner_topic.getSelectedItem().toString();
                } else if(editText_newTopic.length() > 0 && (spinner_topic.getSelectedItem().toString().equals("SELECT TOPIC") == false)) {
                    topic = "";
                }
                // get question and answer
                question = editText_question.getText().toString();
                answer = editText_answer.getText().toString();
                // get isHard value
                if(isHard.isChecked()) {
                    hard = 1;
                } else {
                    hard = 0;
                }
                // verifying all values are valid
                if(topic.length() > 2 && question.length() > 2 && answer.length() > 2) {
                    // insert data into database
                    dbHelper.insertData(topic, question, answer, hard);
                    // notification
                    dialogBox.setTitle("SUCCESS");
                    dialogBox.setMessage("Data Saved Successfully");
                    dialogBox.create().show();
                    // re-populate spinner of topic values
                    setSpinnerValues();
                    // reset all the fields
                    spinner_topic.setSelection(0);
                    editText_newTopic.setText("");
                    editText_question.setText("");
                    editText_answer.setText("");
                    isHard.setChecked(false);
                } else {
                    // show error
                    dialogBox.setTitle("ERROR");
                    dialogBox.setMessage("INVALID VALUES");
                    dialogBox.create().show();
                }
            }
        });

    }

    public void setSpinnerValues() {
        // get unique values of topics
        unique_topics = dbHelper.getUniqueTopics();
        // adding unique topics to spinner_topic
        ArrayAdapter<String> spinner_topicAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinner_topicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_topic.setAdapter(spinner_topicAdapter);
        spinner_topicAdapter.add("SELECT TOPIC");
        for(int i=0; i<unique_topics.size(); i++) {
            spinner_topicAdapter.add(unique_topics.get(i));
        }
        spinner_topicAdapter.notifyDataSetChanged();
    }

}
