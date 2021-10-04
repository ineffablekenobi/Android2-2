package com.example.rough.DTO;

public class User {
    private String userId;
    private String username;
    private Integer highestScore;

    public User(String userId, String username, Integer highestScore) {
        this.userId = userId;
        this.username = username;
        this.highestScore = highestScore;
    }

    public User(String username, Integer highestScore) {
        this.username = username;
        this.highestScore = highestScore;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(Integer highestScore) {
        this.highestScore = highestScore;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
