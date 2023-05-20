package org.firstinspires.ftc.teamcode.movendo;

import com.acmerobotics.dashboard.config.Config;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
@Config
public class MoveTest extends LinearOpMode {
    public static double targetX = 0;
    public static double targetY = 0;
    public static double targetH = 0;

    public static double tP = 0.08, tI, tD;
    public static double hP = 0.008, hI, hD;

    private final PIDController controllerX = new PIDController(tP, tI, tD);
    private final PIDController controllerY = new PIDController(tP, tI, tD);
    private final PIDController controllerH = new PIDController(hP, hI, hD);

    @Override
    public void runOpMode() throws InterruptedException {
        PhotonCore.enable();
        Movendo mv = new Movendo(hardwareMap, telemetry);
        waitForStart();

        while (opModeIsActive()) {
            Pose curPose = mv.getCurrentPose();
            double vx = -controllerX.calculate(targetX, curPose.x);
            double vy = controllerY.calculate(targetY, curPose.y);
            double vh = controllerH.calculate(targetH, Math.toDegrees(curPose.h));

            if (gamepad1.b)
                mv.setMotorPowers(mv.getMotorPowers(Math.min(vx, 0.4), Math.min(vy, 0.4), Math.min(vh, 0.4), curPose.h));
            else
                mv.setMotorPowers(mv.getMotorPowers(0, 0, 0));

            mv.localizer.update();
            telemetry.update();
            updatePIDCoefficients();
        }
    }


    private void updatePIDCoefficients() {
        controllerX.kP = controllerY.kP = tP;
        controllerX.kI = controllerY.kI = tI;
        controllerX.kD = controllerY.kD = tD;
        controllerH.kP = hP;
        controllerH.kI = hI;
        controllerH.kD = hD;
    }
}
