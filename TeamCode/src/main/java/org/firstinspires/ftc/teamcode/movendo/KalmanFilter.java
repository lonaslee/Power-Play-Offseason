package org.firstinspires.ftc.teamcode.movendo;


public class KalmanFilter {
    private double x = 0;
    private double Q;
    private double R;
    private double p = 1;

    public KalmanFilter(double Q, double R) {
        this.Q = Q;
        this.R = R;
    }

    public KalmanFilter() {
        this(0.1, 0.4);
    }

    private double x_previous = x;
    private double p_previous = p;
    private double input1_previous = 0;

    public double estimate(double input1, double input2) {
        double u = input1 - input1_previous;
        x = x_previous + u;

        p = p_previous + Q;

        double k = p / (p + R);

        x = x + k * (input2 - x);

        p = (1 - k) * p;

        x_previous = x;
        p_previous = p;
        input1_previous = input1;

        return x;
    }
}
