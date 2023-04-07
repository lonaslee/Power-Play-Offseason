package org.firstinspires.ftc.teamcode;

import static java.lang.Double.NaN;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.jetbrains.annotations.NotNull;

@Config
public class Arm {
    private final ServoImplEx servo1;
    private final ServoImplEx servo2;
    private final ServoImplEx servo3;
    private final ServoImplEx servo4;

    private final ArmKinematics kinematics = new ArmKinematics(LENGTH_1, LENGTH_2, LENGTH_3);

    public Arm(@NotNull HardwareMap hardwareMap) {
        servo1 = (ServoImplEx) hardwareMap.get("servo1");
        servo2 = (ServoImplEx) hardwareMap.get("servo2");
        servo3 = (ServoImplEx) hardwareMap.get("servo3");
        servo4 = (ServoImplEx) hardwareMap.get("servo4");

        setState(STOWED);
    }

    private double angle1 = NaN;
    private double angle2 = NaN;
    private double angle3 = NaN;
    private double angle4 = NaN;

    private ArmState state = new ArmState(NaN, NaN, NaN);

    @NotNull
    public ArmState getState() {
        return state;
    }

    public static double OFFSET_1 = 135;
    public static double OFFSET_2 = 260;
    public static double OFFSET_3 = -75;
    public static double OFFSET_4 = 250;

    /**
     * Set the state of the arm.
     * This has no effect when the new state equals the current state.
     * If the arm cannot physically go to the requested point, this method
     * will return false, otherwise it will return true.
     */
    public boolean setState(@NotNull ArmState newstate) {
        if (state.equals(newstate)) return true;

        var h = kinematics.inverseCoordinates(newstate.x, newstate.z);
        var v = kinematics.inverseKinematics(h[0], newstate.y);
        if (v == null) return false;

        angle1 = h[1];
        angle2 = v[newstate.side][0];
        angle3 = v[newstate.side][1];
        angle4 = v[newstate.side][2];

        System.out.println("ANGLES: " + angle1 + " " + angle2 + " " + angle3 + " " + angle4);

        servo1.setPosition(toPosition(angle1) + toPosition(OFFSET_1));
        servo2.setPosition(toPosition(angle2) + toPosition(OFFSET_2));
        servo3.setPosition(toPosition(angle3) + toPosition(OFFSET_3));
        servo4.setPosition(toPosition(angle4) + toPosition(OFFSET_4));

        System.out.println("POSITIONS " + servo1.getPosition() + " " + servo2.getPosition() + " " + servo3.getPosition() + " " + servo4.getPosition());

        state = newstate;
        return true;
    }

    public boolean isBusy() {
        return false;
    }

    public void update() {
        var repr = state.toString() + "\nturretAngle:" + angle1 + "\nangle1: " + angle2 + "\nangle2: " + angle3 + "\nangle3:" + angle4;
        System.out.println("Arm.update()");
        System.out.println(repr);
        System.out.println();
    }

    private static final double POSITION_TO_DEGREES = 270.0 / 1.0;
    private static final double DEGREES_TO_POSITION = 1.0 / 270.0;

    private static double toPosition(double degrees) {
        return degrees * DEGREES_TO_POSITION;
    }

    private static double toDegree(double position) {
        return position * POSITION_TO_DEGREES;
    }

    public static final double LENGTH_1 = 10;
    public static final double LENGTH_2 = 10;
    public static final double LENGTH_3 = 10;

    public static final ArmState STOWED = new ArmState(5, 0, 0, 90);
    public static final ArmState EXTENDED = new ArmState(LENGTH_1 + LENGTH_2, 0, 0);
    public static final ArmState LEFT = new ArmState(0, 0, LENGTH_1 + LENGTH_2);
    public static final ArmState RIGHT = new ArmState(0, 0, -LENGTH_1 - LENGTH_2);
    public static final ArmState BACK = new ArmState(-LENGTH_1 - LENGTH_2, 0, 0);
}
