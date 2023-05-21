package org.firstinspires.ftc.teamcode.movendo;


public class AsymetricalTrapezoidalProfile {
    public final double start;
    public final double end;
    public final double mV;
    public final double sA;
    public final double eA;
    public final double distance;
    public final double duration;
    public final boolean reversed;

    private final double[][] phases;

    public AsymetricalTrapezoidalProfile(double start, double end, double sA, double mV, double eA) {
        this.start = start;
        this.end = end;
        this.mV = mV;
        this.sA = sA;
        this.eA = eA;

        final var dist = end - start;
        reversed = dist < 0;
        if (reversed) distance = -dist;
        else distance = dist;

        final var accelTime = mV / sA;
        final var decelTime = mV / eA;
        final var coastTime = distance / mV - accelTime - decelTime;

        if (coastTime >= 0) {
            phases = new double[][]{{sA, accelTime}, {0, coastTime}, {-eA, decelTime}};
        } else {
            final var mA = Math.min(sA, eA);
            final var accdecTime = Math.sqrt(distance / mA);

            phases = new double[][]{{mA, accdecTime}, {-mA, accdecTime}};
        }

        if (reversed) {
            final var tmp = phases[0];
            phases[0] = phases[phases.length - 1];
            phases[phases.length - 1] = tmp;
        }

        var duration = 0.0;
        for (double[] phase : phases)
            duration += phase[1];
        this.duration = duration;
    }

    /**
     * Returns double[a, v, x] at time t, where t is time in seconds.
     */
    public double[] at(double t) {
        double[] res = null;

        var v0 = 0.0;
        var x0 = start;
        double a = sA;
        for (double[] phase : phases) {
            a = phase[0];
            final var dt = phase[1];

            if (t < dt) {
                res = new double[]{a, v0 + a * t, x0 + v0 * t + a * (t * t) / 2};
                break;
            }

            x0 += v0 * dt + a * (dt * dt) / 2;
            v0 += a * dt;

            t -= dt;
        }
        if (res == null) res = new double[]{a, v0, x0};
        if (Double.isNaN(res[2])) res[2] = end;
        if (Double.isNaN(res[1])) res[1] = 0;

        return res;
    }

    public boolean isFinished(double t) {
        return t > duration;
    }

    public double[][] getPhases() {
        return phases.clone();
    }
}
