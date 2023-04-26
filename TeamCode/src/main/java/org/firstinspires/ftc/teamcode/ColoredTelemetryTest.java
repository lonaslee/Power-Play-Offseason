package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class ColoredTelemetryTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        PhotonCore.enable();
        FormattedLineBuilder.initTelemetry(telemetry);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();

        while (opModeIsActive()) {
            var s = new FormattedLineBuilder().red()
                    .add("red string")
                    .nl()
                    .orange()
                    .add("orange string")
                    .nl()
                    .addData("data", 543, "yellow", "orange")
                    .toString();
            System.out.println(s);
            telemetry.addLine(s);
            telemetry.update();
        }
    }
}
