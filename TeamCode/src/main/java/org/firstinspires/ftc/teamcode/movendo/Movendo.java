package org.firstinspires.ftc.teamcode.movendo;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.function.DoubleSupplier;


@Config
public class Movendo {
    public static double mA = 10;
    public static double mV = 10;
    public static double maA = 10;
    public static double maV = 10;

    public static double tP = 0.08;
    public static double tI = 0;
    public static double tD = 0;
    public static double hP = 0.08;
    public static double hI = 0;
    public static double hD = 0;

    public static double sP = 0.04;
    public static double sV = 0.04;
    public static double saP = 0.04;
    public static double saV = 0.04;

    public static int controller = 0;

    private final DcMotorEx[] motors;
    private final BNO055IMU imu;
    private final VoltageSensor voltageSensor;

    public final MecanumLocalizer localizer;

    private final Telemetry tm;

    public Movendo(HardwareMap hardwareMap, Telemetry tm) {
        motors = new DcMotorEx[]{
                (DcMotorEx) hardwareMap.get("fl"),
                (DcMotorEx) hardwareMap.get("bl"),
                (DcMotorEx) hardwareMap.get("br"),
                (DcMotorEx) hardwareMap.get("fr"),
        };
        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        motors[2].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[3].setDirection(DcMotorSimple.Direction.REVERSE);
        imu = (BNO055IMU) hardwareMap.get("imu");
        imu.initialize(new BNO055IMU.Parameters());
        voltageSensor = hardwareMap.voltageSensor.iterator().next();

        localizer = new MecanumLocalizer(new DoubleSupplier[]{
                motors[0]::getCurrentPosition,
                motors[1]::getCurrentPosition,
                motors[2]::getCurrentPosition,
                motors[3]::getCurrentPosition
        }, () -> imu.getAngularOrientation().firstAngle, tm);
        this.tm = tm;
    }

    public Pose getCurrentPose() {
        return localizer.getCurrentPose();
    }

    private Pose targetPose = new Pose(0, 0, 0);

    public Pose getTargetPose() {
        return targetPose;
    }

    public void setTargetPose(Pose newTargetPose) {
        if (targetPose.equals(newTargetPose)) return;

        this.targetPose = newTargetPose;

        profileX = new TrapezoidalProfile(getCurrentPose().x, targetPose.x, mA, mV);
        profileY = new TrapezoidalProfile(getCurrentPose().y, targetPose.y, mA, mV);
        profileH = new TrapezoidalProfile(getCurrentPose().h, targetPose.h, maA, maV);
        profileTimer.reset();
    }

    private TrapezoidalProfile profileX = new TrapezoidalProfile(0, 0, mA, mV);
    private TrapezoidalProfile profileY = new TrapezoidalProfile(0, 0, mA, mV);
    private TrapezoidalProfile profileH = new TrapezoidalProfile(0, 0, maA, maV);
    private final ElapsedTime profileTimer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);

    private final PIDController controllerX = new PIDController(tP, tI, tD);
    private final PIDController controllerY = new PIDController(tP, tI, tD);
    private final PIDController controllerH = new PIDController(hP, hI, hD);

    private final StateFeedback feedbackX = new StateFeedback(sP, sV);
    private final StateFeedback feedbackY = new StateFeedback(sP, sV);
    private final StateFeedback feedbackH = new StateFeedback(saP, saV);

    private boolean busy = false;

    public boolean isBusy() {
        return busy;
    }

    private Pose previousPose = new Pose(0, 0, 0);

    public void update() {
        localizer.update();
        final Pose currentPose = getCurrentPose();

        final double t = profileTimer.time();
        busy = profileX.isFinished(t) && profileY.isFinished(t) && profileH.isFinished(t);
        final double[] atX = profileX.at(t);
        final double[] atY = profileY.at(t);
        final double[] atH = profileH.at(t);

        final double vx;
        final double vy;
        final double vh;

        if (controller == 0) {
            vx = controllerX.calculate(atX[2], currentPose.x);
            vy = controllerY.calculate(atY[2], currentPose.y);
            vh = controllerH.calculate(atH[2], currentPose.h);
        } else {
            vx = feedbackX.calculate(atX[2], atX[1], currentPose.x, (currentPose.x - previousPose.x));
            vy = feedbackY.calculate(atY[2], atY[1], currentPose.y, (currentPose.y - previousPose.y));
            vh = feedbackH.calculate(atH[2], atH[1], currentPose.h, (currentPose.h - previousPose.h));
        }

        setMotorPowers(getMotorPowers(-vx, vy, vh, currentPose.h));

        previousPose = currentPose;

        updatePIDCoefficients();

        if (tm != null) {
            tm.addData("vx", vx);
            tm.addData("vy", vy);
            tm.addData("vh", vh);
            tm.addData("targetX", atX[2]);
            tm.addData("targetY", atY[2]);
            tm.addData("targetH", atH[2]);
        }
    }

    private void updatePIDCoefficients() {
        controllerX.kP = controllerY.kP = tP;
        controllerX.kI = controllerY.kI = tI;
        controllerX.kD = controllerY.kD = tD;
        controllerH.kP = hP;
        controllerH.kI = hI;
        controllerH.kD = hD;

        feedbackX.kP = feedbackY.kP = sP;
        feedbackX.kV = feedbackY.kV = sV;
        feedbackH.kP = saP;
        feedbackH.kV = saV;
    }

    public double[] getMotorPowers(double x, double y, double t, double yaw) {
        final double rotX = x * cos(-yaw) - y * sin(-yaw);
        final double rotY = x * sin(-yaw) + y * cos(-yaw);
        return new double[]{
                rotY + rotX + t,
                rotY - rotX + t,
                rotY + rotX - t,
                rotY - rotX - t
        };
    }

    public double[] getMotorPowers(double x, double y, double t) {
        return new double[]{
                y + x + t,
                y - x + t,
                y + x - t,
                y - x - t
        };
    }

    public void setMotorPowers(double[] powers) {
        for (int i = 0; i < 4; i++)
            motors[i].setPower(12 / voltageSensor.getVoltage() * powers[i]);
    }
}
