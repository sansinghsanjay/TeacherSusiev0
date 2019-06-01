package com.example.root.teachersusiev0;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    // GUI declaration
    TextView textView_id, textView_topic;
    EditText editText_question, editText_answer;
    Button button_prevQ, button_showAns, button_nextQ, button_delQ, button_saveQ, button_editQ;
    CheckBox isHard;
    AlertDialog.Builder dialogBox, deleteDialogBox;

    // other vars
    GlobalVars globalVars;
    DBHelper dbHelper;
    List<Integer> ids;
    int ids_ptr;
    String answer;
    int hard;
    boolean isAnswerToQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
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

        // deleteDialogBox
        deleteDialogBox = new AlertDialog.Builder(this);
        deleteDialogBox.setTitle("DELETE CONFIRMATION");
        deleteDialogBox.setMessage("ARE YOU SURE?");
        deleteDialogBox.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // get id
                int tempID = Integer.parseInt(textView_id.getText().toString());
                // delete entry
                boolean delResult = dbHelper.deleteByID(tempID);
                // show message
                if(delResult == true) {
                    dialogBox.setTitle("SUCCESS");
                    dialogBox.setMessage("DELETION IS SUCCESSFUL");
                    dialogBox.create().show();
                    // reducing id pointer
                    ids_ptr -= 1;
                    // if ids_ptr is less than 0, reset it to 0
                    if(ids_ptr < 0) {
                        ids_ptr = 0;
                    }
                    // re-populate ids
                    ids = getIds();
                } else if(delResult == false) {
                    dialogBox.setTitle("FAILURE");
                    dialogBox.setMessage("DELETION IS FAILED");
                    dialogBox.create().show();
                }
            }
        });
        deleteDialogBox.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // globalVars
        globalVars = (GlobalVars) getApplication();

        // getting isAnswerToQues is true / false
        isAnswerToQuestion = globalVars.getIsAnswerToQuestion();
        globalVars.setIsAnswerToQuestion(false);

        // database
        dbHelper = new DBHelper(getApplicationContext(), globalVars.getDatabaseName(), null, globalVars.getDatabaseVersion());

        // init list of ids and pointer
        ids = new ArrayList<>();
        ids_ptr = -1;

        // textView_id
        textView_id = (TextView) findViewById(R.id.testActivity_textView_id);
        textView_id.setEnabled(false);

        // textView_topic
        textView_topic = (TextView) findViewById(R.id.testActivity_textView_topic);
        textView_topic.setEnabled(false);

        // editText_question
        editText_question = (EditText) findViewById(R.id.testActivity_editText_question);
        editText_question.setFocusable(false);
        editText_question.setFocusableInTouchMode(false);
        editText_question.setClickable(false);

        // editText_answer
        editText_answer = (EditText) findViewById(R.id.testActivity_editText_answer);
        editText_answer.setFocusable(false);
        editText_answer.setFocusableInTouchMode(false);
        editText_answer.setClickable(false);

        // isHard checkbox
        isHard = (CheckBox) findViewById(R.id.testActivity_checkBox_isHard);
        isHard.setFocusable(false);
        isHard.setFocusableInTouchMode(false);
        isHard.setClickable(false);

        // get ids
        ids = getIds();

        // setting up first Q&A
        ids_ptr += 1;
        if(ids_ptr < ids.size() && ids_ptr > -1) {
            resetGUIElements();
            setData(ids_ptr);
        } else if(ids_ptr >= ids.size() - 1) {
            // reset ptr
            ids_ptr = ids.size();
            // reset GUI
            resetGUIElements();
            // show error message
            dialogBox.setTitle("ERROR") ;
            dialogBox.setMessage("ALL Q&A ARE DONE: " + ids_ptr + "/" + ids.size());
            dialogBox.create().show();
        } else if(ids_ptr <= -1) {
            // reset ptr
            ids_ptr = -1;
            // reset GUI
            resetGUIElements();
            // show error message
            dialogBox.setTitle("ERROR") ;
            dialogBox.setMessage("NO PREV Q&A: " + ids_ptr + "/" + ids.size());
            dialogBox.create().show();
        }

        // button_showAnswer
        button_showAns = (Button) findViewById(R.id.testActivity_button_showAnswer);
        button_showAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_answer.getText().toString().length() == 0) {
                    editText_answer.setText(answer);
                } else if(editText_answer.getText().toString().length() > 0) {
                    editText_answer.setText("");
                }
            }
        });

        // button_prevQ
        button_prevQ = (Button) findViewById(R.id.testActivity_button_prevQ);
        button_prevQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setting up first Q&A
                ids_ptr -= 1;
                if(ids_ptr < ids.size() && ids_ptr > -1) {
                    resetGUIElements();
                    setData(ids_ptr);
                } else if(ids_ptr >= ids.size() - 1) {
                    // reset ptr
                    ids_ptr = ids.size();
                    // reset GUI
                    resetGUIElements();
                    // show error message
                    dialogBox.setTitle("ERROR") ;
                    dialogBox.setMessage("ALL Q&A ARE DONE: " + ids_ptr + "/" + ids.size());
                    dialogBox.create().show();
                } else if(ids_ptr <= -1) {
                    // reset ptr
                    ids_ptr = -1;
                    // reset GUI
                    resetGUIElements();
                    // show error message
                    dialogBox.setTitle("ERROR") ;
                    dialogBox.setMessage("NO PREV Q&A: " + ids_ptr + "/" + ids.size());
                    dialogBox.create().show();
                }
            }
        });

        // button_nextQ
        button_nextQ = (Button) findViewById(R.id.testActivity_button_nextQ);
        button_nextQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setting up first Q&A
                ids_ptr += 1;
                if(ids_ptr < ids.size() && ids_ptr > -1) {
                    resetGUIElements();
                    setData(ids_ptr);
                } else if(ids_ptr >= ids.size()) {
                    // reset ptr
                    ids_ptr = ids.size();
                    // reset GUI
                    resetGUIElements();
                    // show error message
                    dialogBox.setTitle("ERROR") ;
                    dialogBox.setMessage("ALL Q&A ARE DONE: " + ids_ptr + "/" + ids.size());
                    dialogBox.create().show();
                } else if(ids_ptr <= -1) {
                    // reset ptr
                    ids_ptr = -1;
                    // reset GUI
                    resetGUIElements();
                    // show error message
                    dialogBox.setTitle("ERROR") ;
                    dialogBox.setMessage("NO PREV Q&A: " + ids_ptr + "/" + ids.size());
                    dialogBox.create().show();
                }
            }
        });

        // button_editQ
        button_editQ = (Button) findViewById(R.id.testActivity_button_editQ);
        button_editQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ids_ptr >=0 && ids_ptr < ids.size()) {
                    // disable all buttons except save button
                    button_editQ.setEnabled(false);
                    button_prevQ.setEnabled(false);
                    button_showAns.setEnabled(false);
                    button_nextQ.setEnabled(false);
                    button_delQ.setEnabled(false);
                    // enable save button
                    button_saveQ.setEnabled(true);
                    // enable question and answer field
                    editText_question.setFocusable(true);
                    editText_question.setFocusableInTouchMode(true);
                    editText_question.setClickable(true);
                    editText_answer.setFocusable(true);
                    editText_answer.setFocusableInTouchMode(true);
                    editText_answer.setClickable(true);
                    // enable isHard checkbox
                    isHard.setFocusable(true);
                    isHard.setFocusableInTouchMode(true);
                    isHard.setClickable(true);
                    // set answer in editText_answer
                    editText_answer.setText(answer);
                }
            }
        });

        // button_saveQ
        button_saveQ = (Button) findViewById(R.id.testActivity_button_saveQ);
        button_saveQ.setEnabled(false);
        button_saveQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean tempResult = false;
                // get question and answer value and save it in database
                int tempId = Integer.parseInt(textView_id.getText().toString());
                String tempQ = editText_question.getText().toString();
                String tempAns = editText_answer.getText().toString();
                int tempHard = -1;
                if(isHard.isChecked()) {
                    tempHard = 1;
                } else{
                    tempHard = 0;
                }

                if(isAnswerToQuestion == false) { // question is question, answer is answer
                    tempResult = dbHelper.updateQA(tempId, tempQ, tempAns, tempHard);
                } else {    // answer is question, question is answer
                    tempResult = dbHelper.updateQA(tempId, tempAns, tempQ, tempHard);
                }
                if(tempResult == true) {
                    dialogBox.setTitle("SUCCESS");
                    dialogBox.setMessage("Operation Successful!");
                    dialogBox.create().show();
                    // disable save button
                    button_saveQ.setEnabled(false);
                    // enable edit button
                    button_editQ.setEnabled(true);
                    // enable other buttons
                    button_prevQ.setEnabled(true);
                    button_showAns.setEnabled(true);
                    button_nextQ.setEnabled(true);
                    button_delQ.setEnabled(true);
                    // disable question and answer field
                    editText_question.setFocusable(false);
                    editText_question.setFocusableInTouchMode(false);
                    editText_question.setClickable(false);
                    editText_answer.setFocusable(false);
                    editText_answer.setFocusableInTouchMode(false);
                    editText_answer.setClickable(false);
                    //disable isHard checkbox field
                    isHard.setChecked(false);
                    isHard.setFocusable(false);
                    isHard.setFocusableInTouchMode(false);
                    isHard.setClickable(false);
                    // reset all editText
                    resetGUIElements();
                } else if(tempResult == false) {
                    dialogBox.setTitle("FAILURE");
                    dialogBox.setMessage("Operation Failed!");
                    dialogBox.create().show();
                }
            }
        });

        // button_delQ
        button_delQ = (Button) findViewById(R.id.testActivity_button_deleteQ);
        button_delQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ids_ptr >=0 && ids_ptr < ids.size()) {
                    deleteDialogBox.create().show();
                }
            }
        });

    }

    // function to set data
    public void setData(int id) {
        // get data
        List<String> resultData = dbHelper.getDataFromID(ids.get(id));
        // setting up id
        textView_id.setText("" + ids.get(id));
        // setting data into corresponding field
        if(resultData.size() == 4) {
            textView_topic.setText(resultData.get(0));
            if(isAnswerToQuestion == false) {   // set question in question field
                editText_question.setText(resultData.get(1));
                answer = resultData.get(2);
            } else {    // set answer in question field
                editText_question.setText(resultData.get(2));
                answer = resultData.get(1);
            }
            hard = Integer.parseInt(resultData.get(3));
            if(hard == 1) {
                isHard.setChecked(true);
            } else {
                isHard.setChecked(false);
            }
        } else {
            dialogBox.setTitle("ERROR");
            dialogBox.setMessage("RESULT LENGTH IS NOT 4");
            dialogBox.create().show();
        }
    }

    // function to return ids from database
    public List<Integer> getIds() {
        List<Integer> tempIds = new ArrayList<>();
        // get testMode and topic
        if(globalVars.getTestMode().equals("SEQ") && globalVars.getTopic().equals("NA")) {
            tempIds = dbHelper.getIds(true, null, globalVars.getOnlyHard());
        } else if(globalVars.getTestMode().equals("RAND") && globalVars.getTopic().equals("NA")) {
            tempIds = dbHelper.getIds(false, null, globalVars.getOnlyHard());
        } else if(globalVars.getTestMode().equals("SEQ") && (globalVars.getTopic().equals("NA") == false)) {
            tempIds = dbHelper.getIds(true, globalVars.getTopic(), globalVars.getOnlyHard());
        } else if(globalVars.getTestMode().equals("RAND") && (globalVars.getTopic().equals("NA") == false)) {
            tempIds = dbHelper.getIds(false, globalVars.getTopic(), globalVars.getOnlyHard());
        }
        return tempIds;
    }

    // function to reset GUI Elements
    public void resetGUIElements() {
        answer = "";
        textView_id.setText("");
        textView_topic.setText("");
        editText_question.setText("");
        editText_answer.setText("");
        isHard.setChecked(false);
    }

}
