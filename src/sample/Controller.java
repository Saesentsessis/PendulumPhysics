package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

public class Controller {
    private Pendulum[] balls;
    int timeMillis = 10; float timeStep = 0.02f;
    @FXML private Canvas canvas;
    @FXML void initialize() {
        assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'sample.fxml'.";
        run();
        canvas.setOnMouseClicked(event -> {
            double mX = event.getX(), mY = event.getY();

        });
    }

    private void run() {
        balls = new Pendulum[5];
        balls[0] = new Pendulum(1,(float)canvas.getWidth()/2-120,0, 250, 0, 30, 10);
        balls[1] = new Pendulum(2,(float)canvas.getWidth()/2-60,0, 250, 0, 30, 10);
        balls[2] = new Pendulum(3,(float)canvas.getWidth()/2,0, 250, 0, 30, 10);
        balls[3] = new Pendulum(4,(float)canvas.getWidth()/2+60,0, 250, 0, 30, 10);
        balls[4] = new Pendulum(5,(float)canvas.getWidth()/2+120,0, 250, (float)Math.PI/2, 30, 10);
        //balls[2] = new Pendulum(3,(float)canvas.getWidth()/2-30,0, 250, (float)Math.PI/2, 30, 10);
        timeStep = timeMillis/1000f;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> Update()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void Update() {
        canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        for (Pendulum b: balls) {
            b.Update(timeStep);
        }
        Physics();
        for (Pendulum b:balls) {
            b.draw(canvas.getGraphicsContext2D());
        }
    }

    private void Physics() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        boolean[][] collisions = new boolean[5][];
        for (int i = 0; i < 5; i++) {
            collisions[i] = new boolean[5];
        }
        for (Pendulum current: balls) {
            for (Pendulum last: balls) {
                if (current.id != last.id) {
                    float distance = (float)Math.sqrt(Math.abs((current.bX-last.bX)*(current.bX-last.bX))+Math.abs((current.bY-last.bY)*(current.bY-last.bY)));
                    float radiuses = current.bobRadius+last.bobRadius;
                    if (distance <= radiuses && (!collisions[current.id-1][last.id-1] && !collisions[last.id-1][current.id-1])) {
                        float halfDifference = (radiuses-distance)/2;
                        float halfAngle = halfDifference/current.bobRadius/(float)Math.PI/2;
                        current.angle-=halfAngle;
                        last.angle+=halfAngle;
                        float buff = current.angVel * 0.99f;
                        current.angVel = last.angVel * 0.99f;
                        last.angVel = buff;
                        current.Update(0);
                        last.Update(0);
                        //b = true;
                        gc.strokeLine(current.bX,current.bY,last.bX,last.bY);
                        collisions[current.id-1][last.id-1] = true;
                    }
                }
            }
        }
        gc.setStroke(Color.BLACK);
    }
}
