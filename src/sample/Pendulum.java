package sample;

import javafx.scene.canvas.GraphicsContext;

public class Pendulum {
    protected float oX;
    protected float oY;
    protected float bX;
    protected float bY;
    protected float bobRadius;
    protected float armLength;
    protected float angle;
    protected float angVel;
    protected float andAcc;
    protected float mass;

    public Pendulum(float x, float y, float length, float angle, float radius, float mass) {
        oX = x;
        oY = y;
        this.angle = angle;
        armLength = length;
        bobRadius = radius;
        this.mass = mass;
        bX = oX + armLength * (float)Math.sin(angle);
        bY = oY + armLength * (float)Math.cos(angle);
    }

    public void Update() {
        float sin = (float)Math.sin(angle);
        bX = oX + armLength * sin;
        bY = oY + armLength * (float)Math.cos(angle);

        andAcc = -0.01f * sin;

        angle += angVel;
        angVel += andAcc;
        angVel *= 0.99f;
    }

    public void draw(GraphicsContext gc) {
        gc.strokeLine(oX, oY, bX, bY);
        gc.fillOval(bX-bobRadius/2,bY-bobRadius/2,bobRadius,bobRadius);
    }
}
