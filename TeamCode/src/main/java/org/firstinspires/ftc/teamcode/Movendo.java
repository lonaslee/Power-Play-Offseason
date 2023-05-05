package org.firstinspires.ftc.teamcode;


import androidx.annotation.NonNull;


import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.DoubleSupplier;

public class Movendo {
    public DoubleSupplier[] encoders;
    public DoubleSupplier imu;

    private final Telemetry tm;

    public void sup() {
        DoubleSupplier[] s = new DoubleSupplier[]{() -> 0, () -> 0, () -> 0, () -> 0};
    }

    /**
     * Constructor.
     *
     * @param encoders DoubleSupplier[fl, bl, br, fr] of encoder values.
     * @param imu      DoubleSupplier of imu angle, in radians.
     */
    public Movendo(DoubleSupplier[] encoders, DoubleSupplier imu, Telemetry telemetry) {
        this.encoders = encoders;
        this.imu = imu;
        tm = telemetry;
    }

    /**
     * Constructor without telemetry.
     */
    public Movendo(DoubleSupplier[] encoders, DoubleSupplier imu) {
        this(encoders, imu, null);
    }

    private Pose pose = new Pose(0, 0, 0);

    public void setPose(Pose pose) {
        this.pose = pose;
    }

    public Pose getPose() {
        return pose;
    }

    private double prevHeading = 0;

    public void update() {
        final double fl = encoders[0].getAsDouble();
        final double bl = encoders[1].getAsDouble();
        final double br = encoders[2].getAsDouble();
        final double fr = encoders[3].getAsDouble();
        final double heading = imu.getAsDouble();

        final double vx = (fl + bl + br + fr) / 4;
        final double vy = ((bl + fr - fl - bl) / LATERAL_MULTIPLIER) / 4;

        final double dh = heading - prevHeading;
        final double sineTerm;
        final double cosTerm;
        if (dh < 1e-6) {
            sineTerm = 1.0 - dh * dh / 6.0;
            cosTerm = dh / 2.0;
        } else {
            sineTerm = Math.sin(dh) / dh;
            cosTerm = (1 - Math.cos(dh)) / dh;
        }

        final double fx = sineTerm * vx - cosTerm * vy;
        final double fy = cosTerm * vx + sineTerm * vy;
        pose = new Pose(pose.x + fx, pose.y + fy, heading);

        if (tm != null) {
            tm.addData("[fl, bl, br, fr]", Arrays.toString(new double[]{fl, bl, br, fr}));
            tm.addData("imuYaw", heading);

            tm.addData("vx-vy", vx + " - " + vy);
            tm.addData("pose", pose);
        }

        prevHeading = heading;
    }

    public static final double LATERAL_MULTIPLIER = 1;

    public static final class Pose {
        public final double x;
        public final double y;
        public final double h;

        public Pose(double x, double y, double h) {
            this.x = x;
            this.y = y;
            this.h = h;
        }

        public boolean equals(Pose o) {
            return hashCode() == o.hashCode();
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, h);
        }

        @NonNull
        @Override
        public String toString() {
            return "(" + x + ", " + y + ") -> " + h + " ";
        }
    }
}
