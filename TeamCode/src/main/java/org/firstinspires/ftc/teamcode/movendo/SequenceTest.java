package org.firstinspires.ftc.teamcode.movendo;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Map;


@TeleOp
public class SequenceTest extends LinearOpMode {
    private final Gamepad pgp1 = new Gamepad();
    private final Gamepad tgp1 = new Gamepad();

    @Override
    public void runOpMode() {
        PhotonCore.enable();
        final LynxModule[] hubs = new LynxModule[]{PhotonCore.CONTROL_HUB, PhotonCore.EXPANSION_HUB};
        for (LynxModule hub : hubs)
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);

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
            long start = System.nanoTime();
            for (LynxModule hub : hubs)
                hub.clearBulkCache();

            try {
                pgp1.copy(tgp1);
                tgp1.copy(gamepad1);
            } catch (RobotCoreException e) {
                throw new RuntimeException(e);
            }

            if (tgp1.y && !pgp1.y) {
                started = true;
            }

            if (!mv.isBusy() && started) {
                if (idx != 4)
                    mv.setTargetPose(poses[idx++]);
            }
            mv.update();

            TelemetryPacket packet = new TelemetryPacket();
            MecanumLocalizer.drawRobot(packet.fieldOverlay(), mv.getCurrentPose(), "#3F51B5");
            MecanumLocalizer.drawRobot(packet.fieldOverlay(), mv.getCurrentTargetPose(), "4CAF50");
            packet.putAll(Map.of(
                    "currentX", mv.getCurrentPose().x,
                    "currentY", mv.getCurrentPose().y,
                    "currentH", mv.getCurrentPose().hdeg,
                    "targetX", mv.getCurrentTargetPose().x,
                    "targetY", mv.getCurrentTargetPose().y,
                    "targetH", mv.getCurrentTargetPose().hdeg
            ));
            FtcDashboard.getInstance().sendTelemetryPacket(packet);

            telemetry.addData("hz", ((1f / (System.nanoTime() - start) / 1e9f)));
            telemetry.update();
        }
    }
}
