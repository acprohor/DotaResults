package com.example.acpro.dotaresults;

public class Match {
    private String teamL;
    private String teamR;
    private String score;

    public Match() {
    }

    public Match(String teamL, String teamR, String score) {
        this.teamL = teamL;
        this.teamR = teamR;
        this.score = score;
    }

    public String getTeamL() {
        return teamL;
    }

    public void setTeamL(String teamL) {
        this.teamL = teamL;
    }

    public String getTeamR() {
        return teamR;
    }

    public void setTeamR(String teamR) {
        this.teamR = teamR;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
