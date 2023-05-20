package org.firstinspires.ftc.teamcode.movendo;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.lonaslee.formattedtelemetry.FormattedLineBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
@Config
public class LocalizerTest extends LinearOpMode {
    public static double speed = 0.7;

    @Override
    public void runOpMode() {
        Movendo mv = new Movendo(hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            mv.setMotorPowers(mv.getMotorPowers(
                    gamepad1.left_stick_x * speed,
                    -gamepad1.left_stick_y * speed,
                    gamepad1.right_stick_x * speed,
                    mv.getCurrentPose().h)
            );

            mv.localizer.update();
            TelemetryPacket packet = new TelemetryPacket();
            Canvas fieldOverlay = packet.fieldOverlay();
            MecanumLocalizer.drawRobot(fieldOverlay, mv.getCurrentPose(), "#3F51B5");
            FtcDashboard.getInstance().sendTelemetryPacket(packet);

            System.out.println(mv.getCurrentPose().toString());
            telemetry.update();
        }
    }
}
