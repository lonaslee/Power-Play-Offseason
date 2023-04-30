package org.firstinspires.ftc.teamcode;


public class TrapezodialProfile {
    public final double start;
    public final double end;
    public final double mV;
    public final double mA;
    public final double distance;
    public final double duration;

    private final double[][] phases;

    public TrapezodialProfile(double start, double end, double mV, double mA) {
        this.start = start;
        this.end = end;
        this.mV = mV;
        this.mA = mA;

        distance = end - start;
        if (mV / mA < distance / mV) {
            final var accelTime = mV / mA;
            final var coastTime = distance / mV - mV / mA;
            phases = new double[][]{{mA, accelTime}, {0, coastTime}, {-mA, accelTime}};
        } else {
            final var accelTime = Math.sqrt(distance / mA);
            phases = new double[][]{{mA, accelTime}, {-mA, accelTime}};
        }

        var duration = 0.0;
        for (double[] phase : phases)
            duration += phase[1];
        this.duration = duration;
    }

    /**
     * Returns double[a, v, x] at time t;
     */
    public double[] at(double t) {
        var v0 = 0.0;
        var x0 = start;
        double a = mA;
        for (double[] phase : phases) {
            a = phase[0];
            final var dt = phase[1];

            if (t < dt) {
                return new double[]{a, v0 + a * t, x0 + v0 * t + a * (t * t) / 2};
            }

            x0 += v0 * dt + a * (dt * dt) / 2;
            v0 += a * dt;

            t -= dt;
        }
        return new double[]{a, v0, x0};
    }

    public boolean isFinished(double t) {
        return t > duration;
    }
}
