package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.lonaslee.formattedtelemetry.FormattedLineBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
@Config
public class ProfiledServoTest extends LinearOpMode {
    public static double mA = 0.5;
    public static double mV = 0.5;
    public static double mD = 0.5;

    public static double target = 0;

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(
                FormattedLineBuilder.initTelemetry(telemetry),
                FtcDashboard.getInstance().getTelemetry()
        );

        ProfiledServo servo = new ProfiledServo(hardwareMap.servo.get("servo1"), 0);

        waitForStart();

        while (opModeIsActive()) {
            servo.setPosition(target);

            servo.setDefaultAccel(mA);
            servo.setDefaultVel(mV);
            servo.setDefaultDecel(mD);
            servo.update();

            telemetry.addLine(new FormattedLineBuilder()
                    .startSlider(0, 1, servo.getCurrentPosition())
                    .blue()
                    .cyan()
                    .magenta()
                    .toString());
            telemetry.update();
        }
    }
}
