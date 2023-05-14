package org.firstinspires.ftc.teamcode.movendo;


public class StateFeedback {
    public double kP;
    public double kV;

    public StateFeedback(double kP, double kV) {
        this.kP = kP;
        this.kV = kV;
    }

    private double targetPosition = 0;
    private double targetVelocity = 0;

    public void set(double targetPosition, double targetVelocity) {
        this.targetPosition = targetPosition;
        this.targetVelocity = targetVelocity;
    }

    public double get(double currentPosition, double currentVelocity) {
        double errorP = targetPosition - currentPosition;
        double errorV = targetVelocity - currentVelocity;
        return (errorP * kP) + (errorV * kV);
    }

    public double calculate(double targetPosition, double targetVelocity, double currentPosition, double currentVelocity) {
        set(targetPosition, targetVelocity);
        return get(currentPosition, currentVelocity);
    }
}
