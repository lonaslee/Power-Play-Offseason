package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp
public class ServoTester extends LinearOpMode {
    public static double position = 0.0;

    @Override
    public void runOpMode() throws InterruptedException {
        Servo servo = hardwareMap.servo.get("servo4");
        waitForStart();

        while (opModeIsActive()) {
            servo.setPosition(position);
            telemetry.update();
        }
    }
}
