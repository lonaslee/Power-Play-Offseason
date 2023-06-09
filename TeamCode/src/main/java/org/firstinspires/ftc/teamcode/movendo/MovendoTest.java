package org.firstinspires.ftc.teamcode.movendo;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;


@TeleOp
@Config
public class MovendoTest extends LinearOpMode {
    public static double targetX = 0;
    public static double targetY = 0;
    public static double targetH = 0;

    private final Gamepad pgp1 = new Gamepad();
    private final Gamepad tgp1 = new Gamepad();

    @Override
    public void runOpMode() {
        PhotonCore.enable();
        telemetry = new MultipleTelemetry(
                telemetry,
                FtcDashboard.getInstance().getTelemetry()
        );
        Movendo mv = new Movendo(hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            try {
                pgp1.copy(tgp1);
                tgp1.copy(gamepad1);
            } catch (RobotCoreException e) {
                throw new RuntimeException(e);
            }

            long start = System.nanoTime();

            if (tgp1.y && !pgp1.y)
                mv.setTargetPose(new Pose(targetX, targetY, Math.toRadians(targetH)));

            mv.update();
            telemetry.addData("isBusy", mv.isBusy());
            telemetry.addData("hz", ((1.0 / (System.nanoTime() - start) / 1e9f)));
            telemetry.addData("targetPose", mv.getTargetPose().toString());
            telemetry.update();
        }
    }
}
