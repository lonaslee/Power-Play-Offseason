package org.firstinspires.ftc.teamcode.movendo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp
public class ConfigTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx[] motors = new DcMotorEx[]{
                (DcMotorEx) hardwareMap.get("fl"),
                (DcMotorEx) hardwareMap.get("bl"),
                (DcMotorEx) hardwareMap.get("br"),
                (DcMotorEx) hardwareMap.get("fr"),
        };
        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[1].setDirection(DcMotorSimple.Direction.REVERSE);

        var fl = motors[0];
        var bl = motors[1];
        var br = motors[2];
        var fr = motors[3];

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.x) {
                fl.setPower(0.5);
            } else {
                fl.setPower(0);
            }
            if (gamepad1.a) {
                bl.setPower(0.5);
            } else {
                bl.setPower(0);
            }
            if (gamepad1.b) {
                br.setPower(0.5);
            } else {
                br.setPower(0);
            }
            if (gamepad1.y) {
                fr.setPower(0.5);
            } else {
                fr.setPower(0);
            }
        }
    }
}
