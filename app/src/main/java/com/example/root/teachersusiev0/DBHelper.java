package com.example.root.teachersusiev0;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    // vars
    Context context;
    String tableName;
    String columnName[];

    public DBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databaseName, factory, version);
        // init vars
        this.context = context;
        tableName = "QA_Table";
        columnName = new String[] {"id", "topic", "question", "answer", "hard"};
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating table
        db.execSQL("create table " + tableName + " (" + columnName[0] + " integer primary key autoincrement, "
                    + columnName[1] + " text, " + columnName[2] + " text, " + columnName[3] + " text, "
                    + columnName[4] + " integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete current database and create updated one
        if(oldVersion < 2) {
            db.execSQL("drop table if exists + " + tableName + ";");
            onCreate(db);
        }
    }

    // function to get unique topics
    public List<String> getUniqueTopics() {
        // vars
        List<String> uniqueTopics = new ArrayList<>();
        // get readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // execute query
        Cursor resultCursor = db.rawQuery("select distinct " + columnName[1] + " from " + tableName + ";", null);
        // fetch results
        while(resultCursor.moveToNext()) {
            uniqueTopics.add(resultCursor.getString(resultCursor.getColumnIndex(columnName[1])));
        }
        return uniqueTopics;
    }

    // function to insert values
    public void insertData(String topic, String question, String answer, int hard) {
        // get database
        SQLiteDatabase db = this.getWritableDatabase();
        // insert values
        db.execSQL("insert into " + tableName + " (" + columnName[1] + ", " + columnName[2] + ", " +
                columnName[3] + ", " + columnName[4] + ") values ('" + topic + "', '" +
                question + "', '" + answer + "', " + hard + ");");
    }

    // function to get IDs
    public List<Integer> getIds(boolean isSeq, String topic, int onlyHard) {
        // list type variable to store ids
        List<Integer> ids = new ArrayList<>();
        // get readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // if topic is null
        if(topic == null) {
            Cursor resultCursor = db.rawQuery("select " + columnName[0] + " from " + tableName + ";", null);
            while(resultCursor.moveToNext()) {
                int temp = resultCursor.getInt(resultCursor.getColumnIndex(columnName[0]));
                ids.add(temp);
            }
            // sort ids if isSeq is true
            if(isSeq == true) {
                // sort the output ids
                Collections.sort(ids);
            } else {
                // shuffle the output ids
                Collections.shuffle(ids);
            }
        } else {
            // if all, hard and simple, are required
            if(onlyHard == 0) {
                Cursor resultCursor = db.rawQuery("select " + columnName[0] + " from " + tableName + " where " +
                        columnName[1] + "='" + topic + "';", null);
                while (resultCursor.moveToNext()) {
                    int temp = resultCursor.getInt(resultCursor.getColumnIndex(columnName[0]));
                    ids.add(temp);
                }
                // sort ids if isSeq is true
                if (isSeq == true) {
                    // sort the output ids
                    Collections.sort(ids);
                } else {
                    // shuffle the output ids
                    Collections.shuffle(ids);
                }
            } else {    // only HARD are required
                if (onlyHard == 1) {
                    Cursor resultCursor = db.rawQuery("select " + columnName[0] + " from " + tableName + " where " +
                            columnName[1] + "='" + topic + "' and " + columnName[4] + "=1;", null);
                    while (resultCursor.moveToNext()) {
                        int temp = resultCursor.getInt(resultCursor.getColumnIndex(columnName[0]));
                        ids.add(temp);
                    }
                    // sort ids if isSeq is true
                    if (isSeq == true) {
                        // sort the output ids
                        Collections.sort(ids);
                    } else {
                        // shuffle the output ids
                        Collections.shuffle(ids);
                    }
                }
            }
        }
        return ids;
    }

    // get contents from ids
    public List<String> getDataFromID(int id) {
        // result string
        List<String> result = new ArrayList<>();
        // cursor var
        Cursor resultCursor;
        // get readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // get cursor
        resultCursor = db.rawQuery("select " + columnName[1] + ", " + columnName[2] + ", " +
                        columnName[3] + ", " + columnName[4] + " from " + tableName + " where " + columnName[0] + "="
                        + id + ";", null);
        // fetching values
        while(resultCursor.moveToNext()) {
            result.add(resultCursor.getString(resultCursor.getColumnIndex(columnName[1])));
            result.add(resultCursor.getString(resultCursor.getColumnIndex(columnName[2])));
            result.add(resultCursor.getString(resultCursor.getColumnIndex(columnName[3])));
            result.add(resultCursor.getString(resultCursor.getColumnIndex(columnName[4])));
        }
        return result;
    }

    // get contents from search query
    public List<List<String>> getSearchData(String searchQuery) {
        // result string
        List<List<String>> resultRows = new ArrayList<>();
        // cursor var
        Cursor resultCursor;
        // get readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // get cursor
        resultCursor = db.rawQuery("select " + columnName[0] + ", " + columnName[1] + ", " + columnName[2] + ", " +
                columnName[3] + ", " + columnName[4] + " from " + tableName + " where " + columnName[2] + " like '%"
                + searchQuery + "%' or " + columnName[3] + " like '%" + searchQuery + "%'" , null);
        // fetching values
        while(resultCursor.moveToNext()) {
            List<String> resultRow = new ArrayList<>();
            resultRow.add(resultCursor.getString(resultCursor.getColumnIndex(columnName[0])));
            resultRow.add(resultCursor.getString(resultCursor.getColumnIndex(columnName[1])));
            resultRow.add(resultCursor.getString(resultCursor.getColumnIndex(columnName[2])));
            resultRow.add(resultCursor.getString(resultCursor.getColumnIndex(columnName[3])));
            resultRow.add(resultCursor.getString(resultCursor.getColumnIndex(columnName[4])));
            resultRows.add(resultRow);  // add resultRow in resultRows
            resultRow = null;
        }
        return resultRows;
    }

    // to upgrade the question and answer
    public boolean updateQA(int id, String question, String answer, int isHard) {
        try {
            // get writable database
            SQLiteDatabase db = this.getWritableDatabase();
            // run upgrade query
            db.execSQL("update " + tableName + " set " + columnName[2] + "='" + question + "', " +
                    columnName[3] + "='" + answer + "', " + columnName[4] + "=" + isHard + " where " + columnName[0] + "=" + id + ";");
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    // delete by ID
    public boolean deleteByID(int id) {
        try {
            // get database
            SQLiteDatabase db = this.getWritableDatabase();
            // delete entry
            db.execSQL("delete from " + tableName + " where " + columnName[0] + "=" + id + ";");
            return true;
        } catch(Exception e) {
            return false;
        }
    }

}