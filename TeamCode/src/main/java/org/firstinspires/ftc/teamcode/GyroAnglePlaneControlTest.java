package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.arm.Arm;
import org.firstinspires.ftc.teamcode.arm.ArmState;

@TeleOp
public class GyroAnglePlaneControlTest extends LinearOpMode {
    private Arm arm;
    private BNO055IMU imu;

    @Override
    public void runOpMode() {
        PhotonCore.enable();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        arm = new Arm(hardwareMap);
        imu = initIMU(hardwareMap.get(BNO055IMU.class, "imu"));

        waitForStart();

        while (opModeIsActive()) {
            var angles = imu.getAngularOrientation();
            int yaw = (int) angles.firstAngle;
            int pitch = (int) angles.secondAngle;
            int roll = (int) angles.thirdAngle;

            var z = 10;
            var x = Math.tan(yaw) * z;
            var y = Math.tan(pitch) * z;

            var ns = new ArmState(x, y, z);
            telemetry.addData("ns", ns.toString());
            arm.setState(ns);

            telemetry.addData("yaw", yaw);
            telemetry.addData("pitch", pitch);
            telemetry.addData("roll", roll);
            telemetry.addLine(arm.getState().toString());
            telemetry.update();
        }
    }

    private static BNO055IMU initIMU(BNO055IMU imu) {
        var parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);
        return imu;
    }
}
