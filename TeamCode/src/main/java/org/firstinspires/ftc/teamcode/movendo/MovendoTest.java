package org.firstinspires.ftc.teamcode.movendo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp
public class MovendoTest extends LinearOpMode {
    public static double targetX = 0;
    public static double targetY = 0;
    public static double targetH = 0;

    @Override
    public void runOpMode() {
        Movendo mv = new Movendo(hardwareMap, telemetry);

        waitForStart();

        while (opModeIsActive()) {
            mv.setMotorPowers(mv.getMotorPowers(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, mv.getCurrentPose().h));

//            mv.update();
            mv.guidare.update();
            telemetry.update();
        }
    }
}
