package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp
public class ServoTest extends LinearOpMode {
    public static double position = 0.0;

    @Override
    public void runOpMode() {
        Servo servo = hardwareMap.servo.get("servo4");
        servo.setPosition(0);
        waitForStart();

        while (opModeIsActive()) {
            servo.setPosition(position);
            telemetry.addData("pos", servo.getPosition());
            telemetry.update();
        }
    }
}
