package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.outoftheboxrobotics.photoncore.PhotonCore;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.arm.Arm;
import org.firstinspires.ftc.teamcode.arm.ArmState;
import org.jetbrains.annotations.Contract;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@TeleOp
@Config
public class GyroAnglePlaneControlTest extends LinearOpMode {
    public static int X = 12;
    public static int S = 1;

    private Arm arm;
    private BNO055IMU imu;

    private final Queue<Integer> yaws = new LinkedList<>(List.of(0, 0, 0, 0, 0));
    private final Queue<Integer> pitches = new LinkedList<>(List.of(0, 0, 0, 0, 0));
    private final Queue<Integer> rolls = new LinkedList<>(List.of(0, 0, 0, 0, 0));

    @Override
    public void runOpMode() {
        PhotonCore.enable();
        telemetry = new MultipleTelemetry(FormattedLineBuilder.initTelemetry(telemetry), FtcDashboard.getInstance().getTelemetry());

        arm = new Arm(hardwareMap);
        imu = initIMU(hardwareMap.get(BNO055IMU.class, "imu"));

        waitForStart();

        while (opModeIsActive()) {
            final var angles = imu.getAngularOrientation();

            yaws.poll();
            pitches.poll();
            rolls.poll();

            yaws.add((int) -angles.firstAngle);
            pitches.add((int) -angles.secondAngle);
            rolls.add((int) angles.thirdAngle);

            final var avgPitch = pitches.stream().mapToInt(Integer::intValue).sum() / 5;
            final var avgYaw = yaws.stream().mapToInt(Integer::intValue).sum() / 5;
            final var avgRoll = rolls.stream().mapToInt(Integer::intValue).sum() / 5;

            final var y = avgPitch / 4;// Math.tan(avgPitch) * x;
            final var z = avgYaw / 4;// Math.tan(avgYaw) * x;

            arm.setState(new ArmState(X, y, z, S));

            telemetry.addLine(new FormattedLineBuilder()
                    .white()
                    .startSlider(-18, 18, X)
                    .blue()
                    .cyan()
                    .darkGray()
                    .nl()
                    .nl()
                    .add(arm.getState().toString())
                    .nl()
                    .startData("angle4", arm.getState().angle4)
                    .red()
                    .magenta()
                    .toString());
            telemetry.update();
        }
    }

    @NonNull
    @Contract("_ -> param1")
    private static BNO055IMU initIMU(@NonNull BNO055IMU imu) {
        var parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);
        return imu;
    }
}
