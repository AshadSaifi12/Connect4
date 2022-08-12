package com.myfirstgame.connect4game;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
    private static final int COLUMNS = 7;
    private static final int ROW = 6;
    private static final int Circle_Diameter = 80;

    private static final String disc1color = "#24303E";
    private static final String disc2color = "4CAA88";

    private boolean isPlayerOneTurn = true;

    private Disc[][] insertedDiscsArray = new Disc[ROW][COLUMNS];

    @FXML
    public Pane discPane;

    @FXML
    public GridPane rootGridPane;

    @FXML
    public Label PlayerName;

    @FXML
    public TextField textFieldName1;

    @FXML
    public TextField textFieldName2;

    @FXML
    public Button startButton;
    private static boolean isinsertdisk=true;

    private static  String player2 ="PLAYER ONE" ;
    private static  String player1 ="PLAYER TWO" ;
    public void createplayground() {
        textFieldName1.setText("ENTER PLAYER ONE NAME");
        textFieldName2.setText("ENTER PLAYER TWO NAME");
        startButton.setOnAction(actionEvent -> {
            player1=textFieldName1.getText();
            player2=textFieldName2.getText();
            PlayerName.setText(player1);
        });
        Shape rectangleWithHoles = createGameStructuralGrid();
        rootGridPane.add(rectangleWithHoles, 0, 1);
        List<Rectangle> rectangleList = createClickableColumns();
        for (Rectangle rectangle : rectangleList
        ) {
            rootGridPane.add(rectangle, 0, 1);
        }


    }

    private Shape createGameStructuralGrid() {
        Shape rectangleWithHoles = new Rectangle((COLUMNS + 1) * Circle_Diameter, (ROW + 1) * Circle_Diameter);
        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Circle circle = new Circle();
                circle.setRadius(Circle_Diameter / 2);
                circle.setCenterX(Circle_Diameter / 2);
                circle.setCenterY(Circle_Diameter / 2);
                circle.setSmooth(true);
                circle.setTranslateX(col * (Circle_Diameter + 5) + (Circle_Diameter / 4));
                circle.setTranslateY(row * (Circle_Diameter + 5) + (Circle_Diameter / 4));

                rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
            }
        }
        rectangleWithHoles.setFill(Color.WHITE);
        return rectangleWithHoles;
    }

    private List<Rectangle> createClickableColumns() {
        List<Rectangle> rectangleList = new ArrayList<>();
        for (int col = 0; col < COLUMNS; col++) {
            Rectangle rectangle = new Rectangle(Circle_Diameter, ((ROW + 1) * Circle_Diameter));
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col * (Circle_Diameter + 5) + (Circle_Diameter / 4));
            rectangle.setOnMouseEntered(mouseEvent -> rectangle.setFill(Color.valueOf("#eeeeee25")));
            rectangle.setOnMouseExited(mouseEvent -> rectangle.setFill(Color.TRANSPARENT));
            final int column = col;
            rectangle.setOnMouseClicked(mouseEvent -> {
                insertDisc(new Disc(isPlayerOneTurn), column);
            });
            rectangleList.add(rectangle);
        }

        return rectangleList;
    }

    private void insertDisc(Disc disc, int column) {
        int row = ROW - 1;
        while (row >= 0) {
            if ((getDiskIfPresent(row,column) == null)) {
                break;
            }
            row--;
        }
        if (row < 0) {
            return;
        }

       if (isinsertdisk){
           isinsertdisk=false;
           insertedDiscsArray[row][column] = disc;
           discPane.getChildren().add(disc);
           disc.setTranslateX(column * (Circle_Diameter + 5) + (Circle_Diameter / 4));
       }
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), disc);
        translateTransition.setToY(row * (Circle_Diameter + 5) + (Circle_Diameter / 4));
        int CurrentRow = row;

        translateTransition.setOnFinished(actionEvent -> {
            PlayerName.setText(isPlayerOneTurn? player2 : player1);
                isPlayerOneTurn = !isPlayerOneTurn;
            if (gameEnded(CurrentRow, column)){
                gameOver();
            }
            isinsertdisk=true;

        });
        translateTransition.play();

    }

    private void gameOver() {
        String Winner = isPlayerOneTurn? player2 : player1;
        Alert alert =new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("......CONNECT4GAME......");
        alert.setHeaderText(("Winner is: " + Winner));
        alert.setContentText("Want To Play Again?");
        ButtonType yesBtn = new ButtonType("Yes-New Game");
        ButtonType noBtn = new ButtonType("No-EXIT");
        alert.getButtonTypes().setAll(yesBtn,noBtn);
        Platform.runLater(() -> {
            Optional<ButtonType> btnclicked = alert.showAndWait();
            if (btnclicked.isPresent() && btnclicked.get()== yesBtn){
                resetGame();
            }
            else {
                Platform.exit();
                System.exit(0);
            }
        });


    }

    public void resetGame() {
        discPane.getChildren().clear();
        for (int row =0; row <insertedDiscsArray.length;row++)
        {
            for (int col =0; col <insertedDiscsArray.length;col++){
                insertedDiscsArray[row][col]=null;
            }
        }
        isPlayerOneTurn=false;
        textFieldName1.setText("ENTER PLAYER ONE NAME");
        textFieldName2.setText("ENTER PLAYER TWO NAME");
        PlayerName.setText("PLAYER ONE");

        createplayground();
    }

    private boolean gameEnded(int row, int column) {
        List<Point2D> verticalPoints = IntStream.rangeClosed(row -3, row+3 )
                .mapToObj(r -> new Point2D(r, column))
                .collect(Collectors.toList());
        List<Point2D> horizontalPoints = IntStream.rangeClosed(column-3 , column+3 )
                .mapToObj(c -> new Point2D(row, c))
                .collect(Collectors.toList());
        Point2D startpoint1 =new Point2D(row-3,column+3);
        List<Point2D> diogonalpoints = IntStream.rangeClosed(0 , 6 )
                .mapToObj(i -> startpoint1.add(i,-i))
                .collect(Collectors.toList());
        Point2D startpoint2 =new Point2D(row-3,column-3);
        List<Point2D> diogonalpoints2 = IntStream.rangeClosed(0 , 6 )
                .mapToObj(i -> startpoint1.add(i,i))
                .collect(Collectors.toList());
        if (checkCombination(verticalPoints) || checkCombination(horizontalPoints) || checkCombination(diogonalpoints) || checkCombination(diogonalpoints2))
        {
            return true;
        }
        else{
        return false;
        }

    }

    private boolean checkCombination(List<Point2D> points) {
        int chain = 0;
        for (Point2D point : points) {
            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray = (int) point.getY();
            Disc disc = getDiskIfPresent(rowIndexForArray,columnIndexForArray);
            if (disc != null && disc.isPlayerOneMove != isPlayerOneTurn) {
                chain++;
                if (chain == 4) {
                    return true;
                }
            }
            else{
                chain = 0;
            }
        }
        return false;
    }

    private static class Disc extends Circle {
        private final boolean isPlayerOneMove;

        public Disc(boolean isPlayerOneMove) {
            this.isPlayerOneMove = isPlayerOneMove;
            setRadius(Circle_Diameter / 2);
            setFill(isPlayerOneMove ? Color.valueOf(disc1color) : Color.valueOf(disc2color));
            setCenterX(Circle_Diameter / 2);
            setCenterY(Circle_Diameter / 2);
        }
    }

    private Disc getDiskIfPresent(int row ,int column)
    {

        if(row>=ROW || row<0 || column>=COLUMNS || column<0) {
            return null;
        }

        return insertedDiscsArray[row][column];
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}