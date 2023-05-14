package org.firstinspires.ftc.teamcode.movendo;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class Movendo {
    private final DcMotorEx[] motors;

    public Movendo(HardwareMap hardwareMap) {
        motors = new DcMotorEx[]{
                (DcMotorEx) hardwareMap.get("fl"),
                (DcMotorEx) hardwareMap.get("bl"),
                (DcMotorEx) hardwareMap.get("br"),
                (DcMotorEx) hardwareMap.get("fr"),
        };
    }

    private double[] pose = new double[]{0, 0, 0};

    public double[] getPose() {
        return pose.clone();
    }

    public void setPose(double[] pose) {
        this.pose = pose;
    }

    public void updatePose() {
        // TODO
    }
}
