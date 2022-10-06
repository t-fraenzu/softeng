package mse.fhnw.ch.repositories;

public class SavedPolicy {
    private String name1;
    private String name2;
    private String state;
    private String birthYear;
    private String score;

    public void setName1(String name1) {

        this.name1 = name1;
    }

    public String getName1() {
        return name1;
    }

    public void setName2(String name2) {

        this.name2 = name2;
    }

    public String getName2() {
        return name2;
    }

    public void setState(String state) {

        this.state = state;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public String getState() {
        return state;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScore() {
        return score;
    }
}
