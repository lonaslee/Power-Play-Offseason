package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@Config
@TeleOp
public class ServoTest extends LinearOpMode {
    public static double pos1 = 0.0;
    public static double pos2 = 0.0;
    public static double pos3 = 0.0;
    public static double pos4 = 0.0;

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        ServoImplEx servo1 = (ServoImplEx) hardwareMap.get("servo1");
        ServoImplEx servo2 = (ServoImplEx) hardwareMap.get("servo2");
        ServoImplEx servo3 = (ServoImplEx) hardwareMap.get("servo3");
        ServoImplEx servo4 = (ServoImplEx) hardwareMap.get("servo4");
        ServoImplEx[] servos = new ServoImplEx[]{servo1, servo2, servo3, servo4};
        for (ServoImplEx servo : servos) servo.setPwmRange(new PwmControl.PwmRange(500, 2500));
        waitForStart();

        while (opModeIsActive()) {
            servo1.setPosition(pos1);
            servo2.setPosition(pos2);
            servo3.setPosition(pos3);
            servo4.setPosition(pos4);

            telemetry.addData("pos1", servo1.getPosition());
            telemetry.addData("pos2", servo2.getPosition());
            telemetry.addData("pos3", servo3.getPosition());
            telemetry.addData("pos4", servo4.getPosition());
            telemetry.update();
        }
    }

    public static double POSITIONS_IN_DEGREES = 1 / 270.0;

    public static double toPosition(double degrees) {
        return degrees * POSITIONS_IN_DEGREES;
    }
}
