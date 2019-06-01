package com.example.root.teachersusiev0;

import android.app.Application;

public class GlobalVars extends Application {

    // database vars
    private String databaseName;
    private int databaseVersion;

    // other vars
    private String testMode;
    private String topic;
    private int onlyHard;
    private boolean isAnswerToQuestion;
    private String searchQuery;
    private int id;

    // constructor
    public GlobalVars() {
        // init database vars
        databaseName = "QA_DB";
        databaseVersion = 1;
        isAnswerToQuestion = false;
    }

    // get method for databaseName
    public String getDatabaseName() {
        return databaseName;
    }

    // get method for databaseVersion
    public int getDatabaseVersion() {
        return databaseVersion;
    }

    // set/get testMode
    public void setTestMode(String testMode) {
        this.testMode = testMode;
    }
    public String getTestMode() {
        return testMode;
    }

    // set/get topic
    public void setTopic(String topic) {
        this.topic = topic;
    }
    public String getTopic() {
        return topic;
    }

    // set/get onlyHard
    public void setOnlyHard(int onlyHard) {
        this.onlyHard = onlyHard;
    }
    public int getOnlyHard() {
        return onlyHard;
    }

    // set/get isAnswerToQuestion
    public void setIsAnswerToQuestion(boolean isAnswerToQuestion) {
        this.isAnswerToQuestion = isAnswerToQuestion;
    }
    public boolean getIsAnswerToQuestion() {
        return isAnswerToQuestion;
    }

    // set/get searchQuery
    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
    public String getSearchQuery() {
        return searchQuery;
    }

    // set/get searchResult
    public void setSearchResultID(int id) {
        this.id = id;
    }
    public int getSearchResultID() {
        return id;
    }

}
