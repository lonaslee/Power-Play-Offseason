package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.ArmKinematics.formatArrayPoint;
import static org.firstinspires.ftc.teamcode.ArmKinematics.roundDecimal;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Config
public class KTest extends LinearOpMode {
    public static double L1 = 10.5;
    public static double L2 = 8;
    public static double L3 = 11;

    public static double X = 0;
    public static double Y = 18.5;
    public static int S = 0;

    private double turretPos = 0.0;

    @Override
    public void runOpMode() {
        var kinematics = new ArmKinematics(L1, L2, L3);

        Servo servo1 = hardwareMap.servo.get("servo1");
        Servo servo2 = hardwareMap.servo.get("servo2");
        Servo servo3 = hardwareMap.servo.get("servo3");
        Servo servo4 = hardwareMap.servo.get("servo4");
        waitForStart();

        while (opModeIsActive()) {
            kinematics.setLengths(L1, L2, L3);
            X += gamepad1.left_stick_x * 0.02;
            Y += -gamepad1.left_stick_y * 0.02;

            turretPos += gamepad1.right_stick_x * 0.001;
            servo1.setPosition(turretPos);

            var v = kinematics.inverseKinematics(X, Y);
            if (v == null) {
                telemetry.addData("kinematics", "UNREACHABLE");
                X -= gamepad1.left_stick_x * 0.02;
                Y -= -gamepad1.left_stick_y * 0.02;
            } else {
                servo2.setPosition(-(toPosition(v[S][0]) + toPosition(Arm.OFFSET_2)));
                servo3.setPosition(toPosition(v[S][1]) + toPosition(Arm.OFFSET_3));
                servo4.setPosition(toPosition(v[S][2]) + toPosition(Arm.OFFSET_4));
                telemetry.addData(
                        "kinematics",
                        roundDecimal(v[S][0]) + ", " + roundDecimal(v[S][1]) + ", " + roundDecimal(v[S][2])
                );
            }
            telemetry.addData("coord", formatArrayPoint(new double[]{X, Y}));
            telemetry.update();
        }
    }

    private static double toPosition(double degree) {
        return degree / 270.0;
    }
}
