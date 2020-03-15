package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class StartTestController {

    @FXML
    private TextField numOfQuestion;

    @FXML
    public String startTest() {
        if (numOfQuestion.getText().matches("\\d+")) {
            int numberQuestion = Integer.parseInt(numOfQuestion.getText());
            if (numberQuestion > 0 && numberQuestion <= 100) {
                return numOfQuestion.getText();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

}
