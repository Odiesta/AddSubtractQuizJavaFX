package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ScoreController {

    @FXML
    private Label rightAns;
    @FXML
    private Label wrongAns;
    @FXML
    private Label score;

    public void initialize() {

    }

    public Label getRightAns() {
        return rightAns;
    }

    public void setRightAns(String rightAns) {
        this.rightAns.setText(rightAns);
    }

    public Label getWrongAns() {
        return wrongAns;
    }

    public void setWrongAns(String wrongAns) {
        this.wrongAns.setText(wrongAns);
    }

    public Label getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score.setText(score);
    }
}
