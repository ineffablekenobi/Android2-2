package com.example.rough;

public class EachPlayerScore {
    String score;
    String player;

    public EachPlayerScore(String score, String player) {
        this.score = score;
        this.player = player;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}
