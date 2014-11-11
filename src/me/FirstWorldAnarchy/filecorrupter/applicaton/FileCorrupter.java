/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.FirstWorldAnarchy.filecorrupter.applicaton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author family
 */
public class FileCorrupter extends Application {

    private final Random rand = new Random();

    private Stage stage;
    private VBox root;
    private List<File> files;
    private final Font font = Font.loadFont(getClass().getResourceAsStream("Roboto-Regular.ttf"), 18);
    private final Font font2 = Font.loadFont(getClass().getResourceAsStream("Roboto-Regular.ttf"), 14);
    private final Text t = new Text("File Corrupter");
    private final Text t1 = new Text();
    private final Button b1 = new Button("Select files to corrupt");
    private final Button b2 = new Button("Corrupt");

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setScene(new Scene(root));
        System.out.println(Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 0.9).toString());
        stage.setWidth(400);
        stage.setHeight(400);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("img/file.png")));
        stage.setTitle("File Corrupter");
        stage.show();
    }

    @Override
    public void init() throws Exception {
        root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #" + Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 0.9).toString().substring(2));
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                root.setStyle("-fx-background-color: #" + Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 0.9).toString().substring(2));
            }
        });

        t.setFont(font);

        b1.setFont(font2);

        b2.setDisable(true);
        b2.setFont(font2);

        t1.setOpacity(0);
        t1.setFont(font2);
        t1.setFill(Color.GREEN);

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    FileChooser chooser = new FileChooser();
                    files = chooser.showOpenMultipleDialog(stage);
                    b2.setDisable(false);
                    b2.setText("Corrupt " + files.size() + " files?");
                    StringBuilder tooltip = new StringBuilder();
                    tooltip.append("Current Files:\n");
                    for (File file : files) {
                        tooltip.append(file.getName()).append("\n");
                    }
                    t1.setFill(Color.GREEN);
                    t1.setText(tooltip.toString());
                    playTextAnimation(t1);
                } catch (NullPointerException ex) {
                    t1.setFill(Color.CRIMSON);
                    t1.setText("Looks like you didn't select a file!");
                    playTextAnimation(t1);
                }
            }
        });

        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (files == null) {
                    return;
                }
                for (File file : files) {
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write("");
                        writer.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FileCorrupter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                t1.setText("Successfully corrupted " + files.size() + " files!");
                playTextAnimation(t1);
            }
        });
        List<Node> children = Arrays.asList(t, b1, b2, t1);
        for (Node node : children) {
            node.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    t.consume();
                }
            });
        }
        root.getChildren().addAll(children);
    }

    public void playTextAnimation(Text t) {
        Timeline timeline = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.ZERO, new KeyValue(t.opacityProperty(), 0));
        KeyFrame kf0 = new KeyFrame(Duration.millis(500), new KeyValue(t.opacityProperty(), 1));
        KeyFrame kf1 = new KeyFrame(Duration.seconds(3), new KeyValue(t.opacityProperty(), 1));
        KeyFrame kf2 = new KeyFrame(Duration.millis(3500), new KeyValue(t.opacityProperty(), 0));
        timeline.getKeyFrames().addAll(kf, kf0, kf1, kf2);
        timeline.play();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
