package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.function.DoubleSupplier;

public class MovendoTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DriveBase db = new DriveBase(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            db.update();
        }
    }
}
