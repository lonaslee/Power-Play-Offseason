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
    public static double L1 = 10;
    public static double L2 = 10;
    public static double L3 = 10;

    public static double X = 0;
    public static double Y = 0;
    public static int S = 0;

    @Override
    public void runOpMode() {
        var kinematics = new ArmKinematics(L1, L2, L3);

        Servo servo1 = hardwareMap.servo.get("servo1");
        Servo servo2 = hardwareMap.servo.get("servo2");
        Servo servo3 = hardwareMap.servo.get("servo3");
        Servo servo4 = hardwareMap.servo.get("servo4");
        waitForStart();

        while (opModeIsActive()) {
            X += gamepad1.left_stick_x * 0.1;
            Y += -gamepad1.left_stick_y * 0.1;

            if (gamepad1.right_stick_x != 0)
                servo1.setPosition(servo1.getPosition() + gamepad1.right_stick_x * 0.05);

            var v = kinematics.inverseKinematics(X, Y);
            if (v == null) {
                telemetry.addData("kinematics", "UNREACHABLE");
            } else {
                servo2.setPosition(toPosition(v[S][1]) + toPosition(Arm.OFFSET_2));
                servo3.setPosition(toPosition(v[S][2]) + toPosition(Arm.OFFSET_3));
                servo4.setPosition(toPosition(v[S][3]) + toPosition(Arm.OFFSET_4));
                telemetry.addData(
                        "kinematics",
                        roundDecimal(v[S][1]) + ", " + roundDecimal(v[S][2]) + ", " + roundDecimal(v[S][3])
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
