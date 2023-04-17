package org.firstinspires.ftc.teamcode;

import static java.lang.Double.NaN;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.jetbrains.annotations.NotNull;

@Config
public class Arm {
    private final ServoImplEx servo1;
    private final ServoImplEx servo2;
    private final ServoImplEx servo3;
    private final ServoImplEx servo4;

    public static double OFFSET_1 = 55;
    public static double OFFSET_2 = -270;
    public static double OFFSET_3 = 75;
    public static double OFFSET_4 = 165;

    private final ElapsedTime timer = new ElapsedTime();

    @NonNull
    private ArmState state = new ArmState(NaN, NaN, NaN);

    public Arm(@NotNull HardwareMap hardwareMap) {
        servo1 = (ServoImplEx) hardwareMap.get("servo1");
        servo2 = (ServoImplEx) hardwareMap.get("servo2");
        servo3 = (ServoImplEx) hardwareMap.get("servo3");
        servo4 = (ServoImplEx) hardwareMap.get("servo4");

        var ns = new ArmState(0, 18.5, 0);
        System.out.println("ns = " + ns);
        setState(ns);
    }

    @NotNull
    public ArmState getState() {
        return state;
    }

    /**
     * Set the state of the arm.
     * This has no effect when the new state equals the current state.
     * If the arm cannot physically go to the requested point, this method
     * will return false, otherwise it will return true.
     */
    public boolean setState(@NotNull ArmState newstate) {
        System.out.println("newstate = " + newstate);
        if (state.equals(newstate)) {
            return true;
        }
        if (!newstate.reachable) {
            return false;
        }

        servo1.setPosition(toPosition(newstate.angle1) + toPosition(OFFSET_1));
        servo2.setPosition(-(toPosition(newstate.angle2) + toPosition(OFFSET_2)));
        servo3.setPosition(toPosition(newstate.angle3) + toPosition(OFFSET_3));
        servo4.setPosition(toPosition(newstate.angle4) + toPosition(OFFSET_4));

        state = newstate;
        timer.reset();
        return true;
    }

    public boolean isBusy() {
        return timer.seconds() < 1;
    }

    public void update() {
        var repr =
                state + "\nturretAngle:" + state.angle1 + "\nangle1: " + state.angle2 + "\nangle2: " + state.angle3 + "\nangle3:" + state.angle4;
        System.out.println(repr);
        System.out.println();
    }

    private static double toPosition(double degrees) {
        return degrees / 270.0;
    }

    private static double toDegree(double position) {
        return position * 270.0;
    }

    private static double clipRange(double num, double min, double max) {
        return Double.min(Double.max(num, min), max);
    }

    private static double clipRange(double num) {
        return clipRange(num, 0, 1);
    }
}
