package org.firstinspires.ftc.teamcode.movendo;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
@Config
public class MovTest extends LinearOpMode {
    public static double targetX = 0;
    public static double targetY = 0;
    public static double targetH = 0;

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Movendo mv = new Movendo(hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            mv.setTargetPose(new Pose(targetX, targetY, targetH));

            mv.update();
            telemetry.update();
        }
    }
}
