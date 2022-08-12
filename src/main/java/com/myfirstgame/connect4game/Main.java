package com.myfirstgame.connect4game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    private Controller controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("game_layout.fxml"));
        GridPane rootGridPane =Loader.load();
        controller =Loader.getController();
        controller.createplayground();
        MenuBar menuBar = createMenu();
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);
        Scene scene = new Scene(rootGridPane);
        stage.setTitle("Connect4Game");
        stage.setScene(scene);
        stage.show();
    }

    private MenuBar createMenu() {
        // file Menu
        Menu menu = new Menu("file");
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(actionEvent -> {
            controller.resetGame();
        });
        MenuItem reset = new MenuItem("Reset Game");
        reset.setOnAction(actionEvent -> {
            controller.resetGame();
        });
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem exit = new MenuItem("EXIT");
        exit.setOnAction(actionEvent -> {
            Platform.exit();
            System.exit(0);
        });
        menu.getItems().addAll(newGame,reset,exit);
        //aboutmenu
        Menu about = new Menu("About");
        MenuItem aboutme =new MenuItem("About Me");
        aboutme.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ABOUT MAKER");
            alert.setHeaderText("ASHAD SAIFI");
            alert.setContentText("HEY!! All This is my First game in JavaFx and Hope U Will All Like it");
            alert.show();
        });
        MenuItem about_game =new MenuItem("About Game");
        about_game.setOnAction(actionEvent -> {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("ABOUT GAME");
            alert1.setHeaderText("Connect-4-Game");
            alert1.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");
            alert1.show();
        });
        about.getItems().addAll(aboutme,separatorMenuItem,about_game);

//  menubar
        MenuBar menuBar = new MenuBar(menu,about);
        return menuBar;
    }

    public static void main(String[] args) {
        launch(args);
    }
}