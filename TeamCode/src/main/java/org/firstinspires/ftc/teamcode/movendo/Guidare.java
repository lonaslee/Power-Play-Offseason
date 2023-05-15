package org.firstinspires.ftc.teamcode.movendo;

import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.function.DoubleSupplier;


@Config
public class Guidare {
    public static double TRACK_WIDTH = 15;
    public static double WHEEL_RADIUS = 1.8898;
    public static double GEAR_RATIO = 1;

    private final DoubleSupplier[] wheelPositions;
    private final DoubleSupplier imuAngle;
    private final Telemetry tm;

    private final ElapsedTime loopTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);

    public Guidare(DoubleSupplier[] wheelPositions, DoubleSupplier imuAngle, Telemetry tm) {
        this.wheelPositions = wheelPositions;
        this.imuAngle = imuAngle;
        this.tm = tm;
    }

    public Guidare(DoubleSupplier[] wheelPositions, DoubleSupplier imuAngle) {
        this(wheelPositions, imuAngle, null);
    }

    private Pose currentPose = new Pose(0, 0, 0);

    public Pose getCurrentPose() {
        return currentPose;
    }

    private double[] previousPositions = new double[4];
    private double previousAngle = 0;

    public void update() {
        final double[] currentPositions = Arrays.stream(wheelPositions).mapToDouble(DoubleSupplier::getAsDouble).toArray();
        final double currentAngle = imuAngle.getAsDouble();

        final double[] lv = new double[4];
        for (int i = 0; i < 4; i++)
            lv[i] = (currentPositions[i] - previousPositions[i]) * (WHEEL_RADIUS * GEAR_RATIO);
        final double av = currentAngle - previousAngle;
        final double t = loopTimer.time();

        final double vy = (lv[fr] + lv[fl] + lv[br] + lv[bl]) / 4;
        final double vx = (lv[bl] + lv[fr] - lv[fl] - lv[br]) / 4;
        final double w = (lv[br] + lv[fr] - lv[fl] - lv[bl]) / (4 * TRACK_WIDTH);

        // @formatter:off
        double sinwt_div_w = t - pow(t, 3) * pow(w, 2) / 6;
        final Matrix deltas = new Matrix(new double[][]{
                { cos(0), -sin(0), 0 },
                { sin(0),  cos(0), 0 },
                {   0   ,    0   , 1 }
        }).mul(new Matrix(new double[][]{
                {    sinwt_div_w   , pow(t, 2) * w / -2, 0 },
                { pow(t, 2) * w / 2,     sinwt_div_w   , 0 },
                {         0        ,         0         , t }
        })).mul(new Matrix(new double[][]{
                { vx },
                { vy },
                { w  }
        }));
        // @formatter:on

        currentPose = new Pose(
                currentPose.x + deltas.get(0, 0),
                currentPose.y + deltas.get(0, 1),
                currentAngle
//                 currentPose.h + deltas.get(0, 2)
        );

        if (tm != null) {
            tm.addData("x", currentPose.x);
            tm.addData("y", currentPose.y);
            tm.addData("heading", Math.toDegrees(currentPose.h));
        }

        previousPositions = currentPositions;
        previousAngle = currentAngle;
        loopTimer.reset();
    }

    private static final int fl = 0;
    private static final int bl = 1;
    private static final int br = 2;
    private static final int fr = 3;
}
