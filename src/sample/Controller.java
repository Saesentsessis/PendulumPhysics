package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class Controller {
    private Pendulum balls;
    @FXML private Canvas canvas;
    @FXML void initialize() {
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'sample.fxml'.";
        run();
        canvas.setOnMouseClicked(event -> {
            double mX = event.getX(), mY = event.getY();

        });
    }

    private void run() {
        balls = new Pendulum((float)canvas.getWidth()/2,0, 250, (float)Math.PI/2, 30, 10);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> Update()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void Update() {
        canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        balls.Update();
        balls.draw(canvas.getGraphicsContext2D());
    }
}
