package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp
@Config
public class Test extends LinearOpMode {
    private final Gamepad prevGp1 = new Gamepad();

    private Arm arm;

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        arm = new Arm(hardwareMap);
        waitForStart();

        while (opModeIsActive()) {
            ArmState newstate = arm.getState().delta(
                    -gamepad1.left_stick_x * 0.1,
                    -gamepad1.right_stick_y * 0.1,
                    -gamepad1.left_stick_y * 0.1,
                    gamepad1.right_stick_x * 0.1
            );
            if (!arm.setState(newstate)) System.out.println("OUT OF RANGE");
            else System.out.println("DELTA TO");

            telemetry.addData("coord", arm.getState().toString());
            telemetry.update();

            try {
                prevGp1.copy(gamepad1);
            } catch (RobotCoreException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
