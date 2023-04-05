package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp
public class ServoTest extends LinearOpMode {
    public static double deg1 = 0.0;
    public static double deg2 = 0.0;
    public static double deg3 = 0.0;
    public static double deg4 = 0.0;

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Servo servo1 = hardwareMap.servo.get("servo1");
        Servo servo2 = hardwareMap.servo.get("servo2");
        Servo servo3 = hardwareMap.servo.get("servo3");
        Servo servo4 = hardwareMap.servo.get("servo4");
        waitForStart();

        while (opModeIsActive()) {
            servo1.setPosition(toPosition(deg1));
            servo2.setPosition(toPosition(deg2));
            servo3.setPosition(toPosition(deg3));
            servo4.setPosition(toPosition(deg4));

            telemetry.addData("pos1" , servo1.getPosition());
            telemetry.addData("pos2" , servo2.getPosition());
            telemetry.addData("pos3" , servo3.getPosition());
            telemetry.addData("pos4" , servo4.getPosition());
            telemetry.update();
        }
    }

    public static double POSITIONS_IN_DEGREES = 1 / 270.0;

    public static double toPosition(double degrees) {
        return degrees * POSITIONS_IN_DEGREES;
    }
}
