package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp
@Config
public class ProfiledServoTest extends LinearOpMode {
    public static double mV = 0.5;
    public static double mA = 0.5;

    public static double target = 0;

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(
                FormattedLineBuilder.initTelemetry(telemetry),
                FtcDashboard.getInstance().getTelemetry()
        );

        ProfiledServo servo = new ProfiledServo((Servo) hardwareMap.get("servo1"), 0);
        waitForStart();

        while (opModeIsActive()) {
            servo.setTarget(target);

            servo.setDefaultVel(mV);
            servo.setDefaultAccel(mA);

            servo.update();

            telemetry.addLine(new FormattedLineBuilder()
                    .startData("curPos", servo.getCurrentPosition())
                    .blue()
                    .cyan()
                    .toString());
            telemetry.addData("x", servo.getCurrentPosition());
            telemetry.update();
        }
    }
}
