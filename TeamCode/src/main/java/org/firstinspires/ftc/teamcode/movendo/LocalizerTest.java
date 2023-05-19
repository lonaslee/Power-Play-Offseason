package org.firstinspires.ftc.teamcode.movendo;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.lonaslee.formattedtelemetry.FormattedLineBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
@Config
public class LocalizerTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(
                FormattedLineBuilder.initTelemetry(telemetry, 100),
                FtcDashboard.getInstance().getTelemetry()
        );
        Movendo mv = new Movendo(hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            mv.setMotorPowers(mv.getMotorPowers(
                    gamepad1.left_stick_x,
                    -gamepad1.left_stick_y,
                    gamepad1.right_stick_x,
                    mv.getCurrentPose().h)
            );

            mv.localizer.update();
            telemetry.update();
        }
    }
}
