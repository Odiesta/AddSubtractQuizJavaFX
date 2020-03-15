package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class Controller {

    @FXML
    private GridPane mainGridpane;
    @FXML
    private VBox vboxQuest;

    private ObservableList<TextField> textFields;
    private ObservableMap<String, String> quesAndAns;
    private ObservableMap<Integer, String> ansIndex;
    private FXMLLoader tempFxmlLoader;

    @FXML
    public boolean newTest() {

        Dialog<ButtonType> dialog = openDialog("Set number question", "startTest.fxml");
        FXMLLoader fxmlLoader = getFXMLLoaderThenClear();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        StartTestController controller = fxmlLoader.getController();
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            vboxQuest.getChildren().clear();
            String numOfQuestions = controller.startTest();
            if (numOfQuestions.equals("")) {
                return false;
            } else {
                quesAndAns = createQuestions(Integer.parseInt(numOfQuestions));
                ansIndex = indexTheAnswer(quesAndAns);
                textFields = displayQuestion(quesAndAns);
                return true;
            }
        }

        return false;

    }

    public Dialog<ButtonType> openDialog(String title, String location) {

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainGridpane.getScene().getWindow());
        dialog.setTitle(title);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(location));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return null;
        }

        tempFxmlLoader = fxmlLoader;

        return dialog;

    }

    public FXMLLoader getFXMLLoaderThenClear() {

        FXMLLoader loader = tempFxmlLoader;
        tempFxmlLoader = null;
        return loader;

    }

    public ObservableMap<String, String> createQuestions(int numOfQuestion) {

        ObservableMap<String, String> quesAndAns =
                FXCollections.observableHashMap();

        Random rand = new Random();

        for (int i = 0; i < numOfQuestion; i++) {

            int rand_int1 = posOrNeg(rand.nextInt(100));
            int rand_int2 = posOrNeg(rand.nextInt(100));

            String addOrSubtract = rand.nextBoolean() ? "+" : "-";

            String question = combineQuestion(rand_int1,
                    rand_int2, addOrSubtract);

            String rightAnswer = "";

            if (addOrSubtract.equals("+")) {
                rightAnswer = Integer.toString(rand_int1 + rand_int2);
            } else {
                rightAnswer = Integer.toString(rand_int1 - rand_int2);
            }
            quesAndAns.put(question, rightAnswer);
        }

        return quesAndAns;

    }

    public int posOrNeg(int number) {

        Random rand = new Random();

        if (!rand.nextBoolean()) {
            return -number;
        } else {
            return number;
        }

    }

    public String combineQuestion(int number1,
                                  int number2, String operation) {

        return determinePosOrNeg(number1) +
                " " + operation + " " +
                determinePosOrNeg(number2) +
                " = ";

    }

    public String determinePosOrNeg(int number) {
        StringBuilder sb = new StringBuilder();
        if (number < 0) {
            sb.append("(").append(number).append(")");
        } else {
            sb.append(number);
        }
        return sb.toString();
    }

    public ObservableMap<Integer, String> indexTheAnswer(
            ObservableMap<String, String> questions) {

        ObservableMap<Integer, String> answers =
                FXCollections.observableHashMap();

        int i = 0;
        for (Map.Entry<String, String> s : questions.entrySet()) {
            answers.put(i, s.getValue());
            i++;
        }
        return answers;

    }

    public ObservableList<TextField> displayQuestion(
            ObservableMap<String, String> map) {

        ObservableList<TextField> textFields =
                FXCollections.observableArrayList();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            Label label = new Label(entry.getKey());

            TextField textField = new TextField();
            textFields.add(textField);
            textField.setPrefSize(75, 20);

            HBox hBox = new HBox(label, textField);
            vboxQuest.getChildren().add(hBox);
        }
        return textFields;

    }

    @FXML
    public void showScore() {

        if (vboxQuest.getChildren().size() == 0) {
            return;
        }

        Dialog<ButtonType> dialog = openDialog("Results", "score.fxml");
        FXMLLoader fxmlLoader = getFXMLLoaderThenClear();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        ScoreController controller = fxmlLoader.getController();
        int[] results = calculateAnswer();

        controller.setRightAns("Right: " + results[0]);
        controller.setWrongAns("Wrong: " + results[1]);
        controller.setScore("Score: " + results[2]);

        dialog.showAndWait();
        quesAndAns.clear();
        ansIndex.clear();
        vboxQuest.getChildren().clear();

    }

    public int[] calculateAnswer() {

        int right = 0, wrong = 0, score;
        int totalQuestion = textFields.size();

        for (int i=0; i<totalQuestion; i++) {
            if (textFields.get(i).getText().matches("\\d+") ||
                textFields.get(i).getText().matches("^-\\d+")) {
                String answer = textFields.get(i).getText();
                if (answer.equals(ansIndex.get(i))) {
                    right++;
                } else {
                    wrong++;
                }
            } else {
                wrong++;
            }
        }

        score = right * 100 / totalQuestion;
        return new int[]{right,wrong,score};

    }

}
