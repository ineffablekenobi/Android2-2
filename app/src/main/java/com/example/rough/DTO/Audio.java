package com.example.rough.DTO;



public class Audio {
    private String DataSource;
    private String ValidAnswer;

    public Audio(String validAnswer, String dataSource) {
        this.ValidAnswer = validAnswer;
        this.DataSource = dataSource;
    }

    public String getDataSource() {
        return DataSource;
    }

    public void setDataSource(String dataSource) {
        DataSource = dataSource;
    }

    public String getValidAnswer() {
        return ValidAnswer;
    }

    public void setValidAnswer(String validAnswer) {
        ValidAnswer = validAnswer;
    }
}
