package org.firstinspires.ftc.teamcode.movendo;

import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.Map;
import java.util.function.DoubleSupplier;


@Config
public class MecanumLocalizer {
    public static double TRACK_WIDTH = 15;
    public static double WHEELBASE = 12;
    public static double WHEEL_RADIUS = 1.8898;
    public static double GEAR_RATIO = 1;

    private final DoubleSupplier[] wheelPositions;
    private final DoubleSupplier imuAngle;
    private final Telemetry tm;

    private final ElapsedTime loopTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
    private final KalmanFilter kalmanFilter = new KalmanFilter();

    public MecanumLocalizer(DoubleSupplier[] wheelPositions, DoubleSupplier imuAngle, Telemetry tm) {
        this.wheelPositions = wheelPositions;
        this.imuAngle = imuAngle;
        this.tm = tm;
    }

    public MecanumLocalizer(DoubleSupplier[] wheelPositions, DoubleSupplier imuAngle) {
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
        final double w = av; //(lv[br] + lv[fr] - lv[fl] - lv[bl]) / (4 * TRACK_WIDTH);

        // @formatter:off
        double sinwt_over_w = t - pow(t, 3) * pow(w, 2) / 6;
        final Matrix deltas = new Matrix(new double[][]{
                { cos(-currentAngle), -sin(-currentAngle), 0 },
                { sin(-currentAngle),  cos(-currentAngle), 0 },
                {         0         ,          0         , 1 }
        }).mul(new Matrix(new double[][]{
                {    sinwt_over_w   , pow(t, 2) * w / -2, 0 },
                { pow(t, 2) * w / 2,     sinwt_over_w   , 0 },
                {         0        ,         0          , t }
        })).mul(new Matrix(new double[][]{
                { vx },
                { vy },
                { w  }
        }));
        // @formatter:on

        final double dx = deltas.get(0, 0);
        final double dy = deltas.get(0, 1);

//        final double rdX = dx * cos(-currentAngle) - dy * sin(-currentAngle);
//        final double rdY = dx * sin(-currentAngle) + dy * cos(-currentAngle);

        currentPose = new Pose(
                currentPose.x + dx,
                currentPose.y + dy,
                currentAngle
        );

        if (tm != null) {
            tm.addData("x", getCurrentPose().x);
            tm.addData("y", getCurrentPose().y);
            tm.addData("h", getCurrentPose().h);
        }

        previousPositions = currentPositions;
        previousAngle = currentAngle;
        loopTimer.reset();
    }

    public static void drawRobot(Canvas fieldOverlay, Pose pose, String color) { // dashboardutil thing
        fieldOverlay.setStroke(color);
        fieldOverlay.setStrokeWidth(1);

        final int ROBOT_RADIUS = 9;
        fieldOverlay.strokeCircle(pose.x, pose.y, ROBOT_RADIUS);
        Pose v = new Pose(cos(pose.h) * ROBOT_RADIUS, sin(pose.h) * ROBOT_RADIUS, 0);
        double x1 = pose.x + v.x / 2;
        double y1 = pose.y + v.y / 2;
        double x2 = pose.x + v.x;
        double y2 = pose.y + v.y;
        fieldOverlay.strokeLine(x1, y1, x2, y2);
    }

    private static final int fl = 0;
    private static final int bl = 1;
    private static final int br = 2;
    private static final int fr = 3;
}
