package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

public class Test extends LinearOpMode {
    private final Gamepad prevGp1 = new Gamepad();

    private Arm arm;

    @Override
    public void runOpMode() throws InterruptedException {
        arm = new Arm(hardwareMap);
        waitForStart();

        while (opModeIsActive()) {
            ArmState newstate;
            if (gamepad1.a && !prevGp1.a) {
                newstate = Arm.STOWED;
                System.out.println("STOW");
            } else if (gamepad1.y && !prevGp1.y) {
                newstate = Arm.EXTENDED;
                System.out.println("EXTEND");
            } else if (gamepad1.x && !prevGp1.x) {
                newstate = Arm.LEFT;
                System.out.println("LEFT");
            } else if (gamepad1.b && !prevGp1.b) {
                newstate = Arm.RIGHT;
                System.out.println("RIGHT");
            } else {
                newstate = arm.getState().delta(
                        gamepad1.left_stick_x,
                        -gamepad1.right_stick_y,
                        gamepad1.left_stick_y,
                        gamepad1.right_stick_x
                );
                if (!newstate.equals(arm.getState()))
                    System.out.println("DELTA TO " + newstate);
            }
            if (!arm.setState(newstate)) {
                System.out.println("OUT OF RANGE");
            }

            arm.update();

            try {
                prevGp1.copy(gamepad1);
            } catch (RobotCoreException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
