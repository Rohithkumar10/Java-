package calculator;



import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SimpleCalculator extends Application {

    private TextField display;

    private double firstNumber = 0;
    private String operator = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        display = new TextField();
        display.setEditable(false);
        display.setStyle("-fx-font-size: 18px;");
        display.setAlignment(Pos.CENTER_RIGHT);

        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10));
        pane.setVgap(5);
        pane.setHgap(5);
        pane.setAlignment(Pos.CENTER);

        String[] buttons = {
                "7", "8", "9", "/", 
                "4", "5", "6", "*", 
                "1", "2", "3", "-", 
                "0", ".", "=", "+"
        };

        int row = 1;
        int col = 0;

        for (String label : buttons) {
            Button button = new Button(label);
            button.setPrefSize(50, 50);
            button.setOnAction(e -> handleButtonClick(label));

            pane.add(button, col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }

        Button clearBtn = new Button("C");
        clearBtn.setPrefSize(50, 50);
        clearBtn.setOnAction(e -> display.clear());
        pane.add(clearBtn, 0, 0);

        pane.add(display, 1, 0, 3, 1);

        Scene scene = new Scene(pane, 240, 300);
        primaryStage.setTitle("Simple Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleButtonClick(String value) {
        switch (value) {
            case "+": case "-": case "*": case "/":
                if (!display.getText().isEmpty()) {
                    firstNumber = Double.parseDouble(display.getText());
                    operator = value;
                    display.clear();
                }
                break;
            case "=":
                if (!operator.isEmpty() && !display.getText().isEmpty()) {
                    double secondNumber = Double.parseDouble(display.getText());
                    double result = calculate(firstNumber, secondNumber, operator);
                    display.setText(String.valueOf(result));
                    operator = "";
                }
                break;
            default:
                display.appendText(value);
        }
    }

    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b == 0) {
                    showAlert("Error", "Cannot divide by zero!");
                    return 0;
                }
                return a / b;
            default: return 0;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

