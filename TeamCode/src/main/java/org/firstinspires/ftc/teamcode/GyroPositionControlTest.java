package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.arm.Arm;
import org.firstinspires.ftc.teamcode.arm.ArmState;

@TeleOp
public class GyroPositionControlTest extends LinearOpMode {
    private Arm arm;
    private BNO055IMU imu;

    @Override
    public void runOpMode() {
        PhotonCore.enable();
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        arm = new Arm(hardwareMap);
        imu = initIMU(hardwareMap.get(BNO055IMU.class, "imu"));
        imu.startAccelerationIntegration(new Position(), new Velocity(), 10);

        waitForStart();

        while (opModeIsActive()) {
            var pos = imu.getPosition().toUnit(DistanceUnit.INCH);
            var pitch = imu.getAngularOrientation().secondAngle;

            arm.setState(new ArmState(pos.x, pos.y, pos.z, pitch));

            telemetry.addLine(arm.getState().toString());
            telemetry.addData("pos", pos.toString());
            telemetry.addData("pitch", pitch);
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
