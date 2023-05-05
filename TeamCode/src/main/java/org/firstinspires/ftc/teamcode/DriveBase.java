package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.function.DoubleSupplier;

public class DriveBase {
    private final DcMotorEx fl;
    private final DcMotorEx bl;
    private final DcMotorEx br;
    private final DcMotorEx fr;
//    public final IMU imu;

    public final Movendo movendo;

    public DriveBase(HardwareMap hardwareMap) {
        fl = (DcMotorEx) hardwareMap.get("lf");
        bl = (DcMotorEx) hardwareMap.get("lb");
        br = (DcMotorEx) hardwareMap.get("rb");
        fr = (DcMotorEx) hardwareMap.get("rf");
        /*
        imu = hardwareMap.get("imu");
        imu.initialize(
            new IMU.Parameters(
                new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                    RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
            )
        );
         */
        movendo = new Movendo(
                new DoubleSupplier[]{fl::getCurrentPosition, bl::getCurrentPosition, br::getCurrentPosition, fr::getCurrentPosition}, /*imu::getYaw*/
                () -> 0
        );
    }

    public void update() {
        movendo.update();
    }


}
