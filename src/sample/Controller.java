package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Controller {
    private Pendulum[] balls;
    int timeMillis = 20, counter = 0; float timeStep = 0.02f, bounciness = 0.998f, inertiaFactor = 1f;
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
        balls[0] = new Pendulum(1,(float)canvas.getWidth()/2-120,0, 250, 0, 30, 10, 0);
        balls[1] = new Pendulum(2,(float)canvas.getWidth()/2-60,0, 250, 0, 30, 10,0);
        balls[2] = new Pendulum(3,(float)canvas.getWidth()/2,0, 250, 0, 30, 10,0);
        balls[3] = new Pendulum(4,(float)canvas.getWidth()/2+60,0, 250, (float)Math.PI/3, 30, 10,0);
        balls[4] = new Pendulum(5,(float)canvas.getWidth()/2+120,0, 250, (float)Math.PI/3, 30, 10,0);
        //balls[2] = new Pendulum(3,(float)canvas.getWidth()/2-30,0, 250, (float)Math.PI/2, 30, 10);
        timeStep = timeMillis/1000f;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5), event -> Update()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void Update() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Pendulum b: balls) {
            b.Update(timeStep);
        }
        float globalVel = Physics();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.strokeText("Force: "+globalVel, canvas.getWidth()/2,canvas.getHeight()*0.9f);
        for (Pendulum b : balls) {
            b.draw(gc);
        }
        gc.strokeText(round(balls[4].angle*180/Math.PI,1)+"", 300, 50);
    }

    private float Physics() {
        float gVEl = 0;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        boolean[][] collisions = new boolean[5][];
        for (int i = 0; i < 5; i++) {
            collisions[i] = new boolean[5];
            gc.strokeText(round(balls[i].angVel,2)+"", 20, 210+i*15);
        }
        float bfactor = 1-(1-bounciness)/2;
        if (counter > 0) {
            for (Pendulum current : balls) {
                for (Pendulum last : balls) {
                    if (current.id != last.id) {
                        float distance = (float) Math.sqrt((current.bX - last.bX) * (current.bX - last.bX) + (current.bY - last.bY) * (current.bY - last.bY));
                        float radiuses = current.bobRadius + last.bobRadius;
                        if (distance <= radiuses && (!collisions[current.id - 1][last.id - 1] && !collisions[last.id - 1][current.id - 1])) {
                            float intersectDist = (radiuses - distance);
                            float intersectAngle = intersectDist / current.armLength * 1.01f;
                            float firstPower = Math.abs(current.angVel) / (Math.abs(current.angVel) + Math.abs(last.angVel));
                            if (current.angVel + last.angVel == 0) firstPower = 0;
                            //System.out.println(last.angle);
                            current.angle -= intersectAngle * firstPower;
                            last.angle += intersectAngle * (1f - firstPower);
                            float buff = current.angVel * bfactor * inertiaFactor;
                            current.angVel = last.angVel * bfactor * inertiaFactor + current.angVel * (1f - inertiaFactor);
                            last.angVel = buff + last.angVel * (1f - inertiaFactor);
                            current.Update(0);
                            last.Update(0);
                            gc.strokeText("" + round(intersectDist / 2, 1), last.id * 20, 10 + current.id * 15);
                            gc.strokeText("" + round(intersectAngle * 180 / Math.PI, 1), last.id * 20, 110 + current.id * 15);
                            collisions[last.id - 1][current.id - 1] = true;
                        }
                    }
                }
            }
        }
        else {
            for (int i = balls.length - 1; i >= 0; i--) {
                Pendulum current = balls[i];
                for (int j = balls.length - 1; j >= 0; j--) {
                    Pendulum last = balls[j];
                    if (current.id != last.id) {
                        float distance = (float) Math.sqrt((current.bX - last.bX) * (current.bX - last.bX) + (current.bY - last.bY) * (current.bY - last.bY));
                        float radiuses = current.bobRadius + last.bobRadius;
                        if (distance < radiuses && (!collisions[current.id - 1][last.id - 1] && !collisions[last.id - 1][current.id - 1])) {
                            float intersectDist = (radiuses - distance);
                            float intersectAngle = intersectDist / current.armLength * 1.01f;
                            float firstPower = Math.abs(current.angVel) / (Math.abs(current.angVel) + Math.abs(last.angVel));
                            if (current.angVel + last.angVel == 0) firstPower = 0;
                            //System.out.println(last.angle);
                            current.angle += intersectAngle * firstPower;
                            last.angle -= intersectAngle * (1f - firstPower);
                            float buff = current.angVel * bfactor * inertiaFactor;
                            current.angVel = last.angVel * bfactor * inertiaFactor + current.angVel * (1f - inertiaFactor);
                            last.angVel = buff + last.angVel * (1f - inertiaFactor);
                            current.Update(0);
                            last.Update(0);
                            gc.strokeText("" + round(intersectDist / 2, 1), last.id * 20, 10 + current.id * 15);
                            gc.strokeText("" + round(intersectAngle * 180 / Math.PI, 1), last.id * 20, 110 + current.id * 15);
                            collisions[current.id - 1][last.id - 1] = true;
                        }
                    }
                }
            }
        }
        counter = -counter;
        for (Pendulum p: balls) {
            gVEl += Math.abs(p.angVel);
        }
        gc.setStroke(Color.BLACK);
        return gVEl;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
