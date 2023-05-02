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
        telemetry = new MultipleTelemetry(
                FormattedLineBuilder.initTelemetry(telemetry),
                FtcDashboard.getInstance().getTelemetry()
        );

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addLine(new FormattedLineBuilder().red()
                    .add("red string")
                    .nl()
                    .orange()
                    .add("orange string")
                    .nl()
                    .startData("label1", "3498573489")
                    .pink()
                    .magenta()
                    .nl()
                    .startData("label2", "dfjaslfjask")
                    .cyan()
                    .blue()
                    .nl()
                    .startSlider(0, 100, 100)
                    .green()
                    .lime()
                    .white()
                    .nl()
                    .startProgressBar(0, 100, 88)
                    .blue()
                    .cyan()
                    .white()
                    .nl()
                    .spinner()
                    .add(" | ")
                    .spinnerBig()
                    .add(" | ")
                    .spinnerLine()
                    .add(" | ")
                    .spinnerNoise()
                    .nl()
                    .add("final string")
                    .toString());
            telemetry.update();
        }
    }
}
