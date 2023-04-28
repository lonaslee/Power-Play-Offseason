package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.arm.Arm;
import org.firstinspires.ftc.teamcode.arm.ArmState;

@TeleOp
@Config
public class Test extends LinearOpMode {
    private final Gamepad prevGp1 = new Gamepad();

    public static int X = 0;
    public static int Y = 18;
    public static int Z = 0;

    private Arm arm;

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        arm = new Arm(hardwareMap);
        waitForStart();

        while (opModeIsActive()) {
            arm.setState(new ArmState(X, Y, Z));

            telemetry.addLine(arm.getState().toString());
            telemetry.update();

            try {
                prevGp1.copy(gamepad1);
            } catch (RobotCoreException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
