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
public class SequenceTest extends LinearOpMode {
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

        Pose[] poses = new Pose[]{
                new Pose(10, 5, Math.toRadians(90)),
                new Pose(0, 0, 0),
                new Pose(50, -5, Math.toRadians(-90)),
                new Pose(0, 0, 0),
                new Pose(50, 0, Math.toRadians(135)),
                new Pose(0, 0, 0)
        };
        int idx = 0;
        waitForStart();

        boolean started = false;

        while (opModeIsActive()) {
            try {
                pgp1.copy(tgp1);
                tgp1.copy(gamepad1);
            } catch (RobotCoreException e) {
                throw new RuntimeException(e);
            }

            if (tgp1.y && !pgp1.y) {
                started = true;
            }

            long start = System.nanoTime();

            if (!mv.isBusy() && started) {
                if (idx != 4)
                    mv.setTargetPose(poses[idx++]);
            }

            mv.update();
            telemetry.addData("isBusy", mv.isBusy());
            telemetry.addData("hz", ((1.0 / (System.nanoTime() - start) / 1e9f)));
            telemetry.addData("targetPose", mv.getTargetPose().toString());
            telemetry.update();
        }
    }
}
