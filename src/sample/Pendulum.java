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
    protected float angAcc;
    protected float mass;
    public int id;

    public Pendulum(int id, float x, float y, float length, float angle, float radius, float mass, float angVel) {
        this.id = id;
        oX = x;
        oY = y;
        this.angle = angle;
        armLength = length;
        bobRadius = radius;
        this.mass = mass;
        bX = oX + armLength * (float)Math.sin(angle);
        bY = oY + armLength * (float)Math.cos(angle);
        this.angVel = angVel;
    }

    public void Update(float timeStep) {
        angle += angVel*timeStep;
        float sin = (float)Math.sin(angle);
        bX = oX + armLength * sin;
        bY = oY + armLength * (float)Math.cos(angle);

        angAcc = -9.8f * sin * timeStep;

        angVel += angAcc*timeStep;
    }

    public void draw(GraphicsContext gc) {
        gc.strokeLine(oX, oY, bX, bY);
        gc.fillOval(bX-bobRadius,bY-bobRadius,bobRadius*2,bobRadius*2);
    }
}
