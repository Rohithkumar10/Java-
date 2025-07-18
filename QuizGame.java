package quizgame;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.*;

public class QuizGame extends Application {
    private Label questionLabel, timerLabel, scoreLabel;
    private RadioButton option1, option2, option3, option4;
    private ToggleGroup optionsGroup;
    private Button nextButton;

    private int currentQuestionIndex = 0;
    private int score = 0;
    private int timeLeft = 10;
    private Timeline timer;

    private List<Question> questions;

    @Override
    public void start(Stage primaryStage) {
        questions = loadQuestions();

        questionLabel = new Label();
        timerLabel = new Label("Time: 10");
        scoreLabel = new Label("Score: 0");

        option1 = new RadioButton();
        option2 = new RadioButton();
        option3 = new RadioButton();
        option4 = new RadioButton();

        optionsGroup = new ToggleGroup();
        option1.setToggleGroup(optionsGroup);
        option2.setToggleGroup(optionsGroup);
        option3.setToggleGroup(optionsGroup);
        option4.setToggleGroup(optionsGroup);

        nextButton = new Button("Next");
        nextButton.setOnAction(e -> checkAnswer());

        VBox vbox = new VBox(10, questionLabel, option1, option2, option3, option4, timerLabel, nextButton, scoreLabel);
        vbox.setStyle("-fx-padding: 20; -fx-font-size: 14;");
        Scene scene = new Scene(vbox, 400, 300);

        primaryStage.setTitle("Quiz Game with Timer");
        primaryStage.setScene(scene);
        primaryStage.show();

        loadNextQuestion();
    }

    private List<Question> loadQuestions() {
        List<Question> qList = new ArrayList<>();
        qList.add(new Question("What is the capital of France?", new String[]{"Paris", "London", "Berlin", "Madrid"}, "Paris"));
        qList.add(new Question("Which planet is known as the Red Planet?", new String[]{"Earth", "Mars", "Venus", "Jupiter"}, "Mars"));
        qList.add(new Question("Who wrote 'Hamlet'?", new String[]{"Charles Dickens", "William Shakespeare", "Tolstoy", "Homer"}, "William Shakespeare"));
        return qList;
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex >= questions.size()) {
            showFinalResult();
            return;
        }

        Question current = questions.get(currentQuestionIndex);
        questionLabel.setText(current.getQuestion());
        String[] opts = current.getOptions();
        option1.setText(opts[0]);
        option2.setText(opts[1]);
        option3.setText(opts[2]);
        option4.setText(opts[3]);
        optionsGroup.selectToggle(null);

        startTimer();
    }

    private void startTimer() {
        timeLeft = 10;
        timerLabel.setText("Time: " + timeLeft);
        if (timer != null) timer.stop();

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);
            if (timeLeft <= 0) {
                timer.stop();
                checkAnswer(); // Auto move if time runs out
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void checkAnswer() {
        timer.stop();

        RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
        if (selected != null) {
            String answer = selected.getText();
            if (answer.equals(questions.get(currentQuestionIndex).getAnswer())) {
                score += 10;
                scoreLabel.setText("Score: " + score);
            }
        }

        currentQuestionIndex++;
        loadNextQuestion();
    }

    private void showFinalResult() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Over");
        alert.setHeaderText("Your Score: " + score);
        alert.setContentText("Thanks for playing!");
        alert.showAndWait();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Question {
    private String question;
    private String[] options;
    private String answer;

    public Question(String question, String[] options, String answer) {
        this.question = question;
        this.options = options;
        this.answer = answer;
    }

    public String getQuestion() 
    {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;
    }
}
