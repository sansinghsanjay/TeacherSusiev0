package com.example.root.teachersusiev0;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.List;

public class SearchTestActivity extends AppCompatActivity {

    // GUI
    EditText editText_id, editText_topic, editText_question, editText_answer;
    CheckBox checkBox_hard;
    Button button_edit, button_save;

    // global variables
    GlobalVars globalVars;
    AlertDialog.Builder dialogBox;

    // database vars
    DBHelper dbHelper;

    // other variables
    int searchResultId;
    boolean isAnswerToQuestion;
    int hard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // global variables
        globalVars = (GlobalVars) getApplication();

        // init dbHelper
        dbHelper = new DBHelper(getApplicationContext(), globalVars.getDatabaseName(), null, globalVars.getDatabaseVersion());

        // back button to parentActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // AlertDialog Box
        dialogBox = new AlertDialog.Builder(this);
        dialogBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //dismiss the dialog
            }
        });

        // editText for id
        editText_id = (EditText) findViewById(R.id.searchTest_editText_id);
        editText_id.setFocusable(false);
        editText_id.setFocusableInTouchMode(false);
        editText_id.setClickable(false);

        // editText for topic
        editText_topic = (EditText) findViewById(R.id.searchTest_editText_topic);
        editText_topic.setFocusable(false);
        editText_topic.setFocusableInTouchMode(false);
        editText_topic.setClickable(false);

        // editText for question
        editText_question = (EditText) findViewById(R.id.searchTest_editText_question);
        editText_question.setFocusable(false);
        editText_question.setFocusableInTouchMode(false);
        editText_question.setClickable(false);

        // editText for answer
        editText_answer = (EditText) findViewById(R.id.searchTest_editText_answer);
        editText_answer.setFocusable(false);
        editText_answer.setFocusableInTouchMode(false);
        editText_answer.setClickable(false);

        // checkBox for hard
        checkBox_hard = (CheckBox) findViewById(R.id.searchTest_checkBox_hard);
        checkBox_hard.setFocusable(false);
        checkBox_hard.setFocusableInTouchMode(false);
        checkBox_hard.setClickable(false);

        // getting isAnswerToQues is true / false
        isAnswerToQuestion = globalVars.getIsAnswerToQuestion();
        globalVars.setIsAnswerToQuestion(false);

        // edit button
        button_edit = (Button) findViewById(R.id.searchTest_button_edit);
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_edit.setEnabled(false);
                button_save.setEnabled(true);

                // editText for question
                editText_question.setFocusable(true);
                editText_question.setFocusableInTouchMode(true);
                editText_question.setClickable(true);

                // editText for answer
                editText_answer.setFocusable(true);
                editText_answer.setFocusableInTouchMode(true);
                editText_answer.setClickable(true);

                // checkBox for hard
                checkBox_hard.setFocusable(true);
                checkBox_hard.setFocusableInTouchMode(true);
                checkBox_hard.setClickable(true);
            }
        });

        // save button
        button_save = (Button) findViewById(R.id.searchTest_button_save);
        button_save.setEnabled(false);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean tempResult = false;
                // get question and answer value and save it in database
                String tempQ = editText_question.getText().toString();
                String tempAns = editText_answer.getText().toString();
                int tempHard = -1;
                if(checkBox_hard.isChecked()) {
                    tempHard = 1;
                } else{
                    tempHard = 0;
                }
                if(isAnswerToQuestion == false) { // question is question, answer is answer
                    tempResult = dbHelper.updateQA(searchResultId, tempQ, tempAns, tempHard);
                } else {    // answer is question, question is answer
                    tempResult = dbHelper.updateQA(searchResultId, tempAns, tempQ, tempHard);
                }
                if(tempResult == true) {
                    dialogBox.setTitle("SUCCESS");
                    dialogBox.setMessage("Operation Successful!");
                    dialogBox.create().show();
                    // disable save button
                    button_save.setEnabled(false);
                    // enable edit button
                    button_edit.setEnabled(true);
                    // disable question and answer field
                    editText_topic.setFocusable(false);
                    editText_topic.setFocusableInTouchMode(false);
                    editText_topic.setClickable(false);
                    editText_question.setFocusable(false);
                    editText_question.setFocusableInTouchMode(false);
                    editText_question.setClickable(false);
                    editText_answer.setFocusable(false);
                    editText_answer.setFocusableInTouchMode(false);
                    editText_answer.setClickable(false);
                    //disable isHard checkbox field
                    checkBox_hard.setFocusable(false);
                    checkBox_hard.setFocusableInTouchMode(false);
                    checkBox_hard.setClickable(false);
                } else if(tempResult == false) {
                    dialogBox.setTitle("FAILURE");
                    dialogBox.setMessage("Operation Failed!");
                    dialogBox.create().show();
                }

            }
        });

        // get search result id
        searchResultId = globalVars.getSearchResultID();
        globalVars.setSearchResultID(-1);
        if(searchResultId < 0) {
            dialogBox.setTitle("Error");
            dialogBox.setMessage("Invalid result ID found.");
            dialogBox.create().show();
        } else {
            // get data
            List<String> resultData = dbHelper.getDataFromID(searchResultId);
            // setting up id
            editText_id.setText("" + searchResultId);
            // setting data into corresponding field
            if(resultData.size() == 4) {
                editText_topic.setText(resultData.get(0));
                if(isAnswerToQuestion == false) {   // set question in question field
                    editText_question.setText(resultData.get(1));
                    editText_answer.setText(resultData.get(2));
                } else {    // set answer in question field
                    editText_question.setText(resultData.get(2));
                    editText_answer.setText(resultData.get(1));
                }
                hard = Integer.parseInt(resultData.get(3));
                if(hard == 1) {
                    checkBox_hard.setChecked(true);
                } else {
                    checkBox_hard.setChecked(false);
                }
            } else {
                dialogBox.setTitle("ERROR");
                dialogBox.setMessage("RESULT LENGTH IS NOT 4");
                dialogBox.create().show();
            }
        }
    }

}
