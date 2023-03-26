package org.firstinspires.ftc.teamcode;

import static java.lang.Double.NaN;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.jetbrains.annotations.NotNull;

public class Arm {
    private final ServoImplEx turret;
    private final ServoImplEx joint1;
    private final ServoImplEx joint2;
    private final ServoImplEx joint3;

    private final ArmKinematics kinematics = new ArmKinematics(LENGTH_1, LENGTH_2, LENGTH_3);

    public Arm(@NotNull HardwareMap hardwareMap) {
        turret = (ServoImplEx) hardwareMap.get("turret");
        joint1 = (ServoImplEx) hardwareMap.get("joint1");
        joint2 = (ServoImplEx) hardwareMap.get("joint2");
        joint3 = (ServoImplEx) hardwareMap.get("joint3");

        setState(state);
    }

    private double turretAngle = NaN;
    private double angle1 = NaN;
    private double angle2 = NaN;
    private double angle3 = NaN;

    private ArmState state = STOWED;

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
        if (state.equals(newstate)) return true;

        var h = kinematics.inverseCoordinates(newstate.x, newstate.z);
        var v = kinematics.inverseKinematics(h[0], newstate.y);
        if (v == null) return false;

        turretAngle = h[1];
        angle1 = v[newstate.side][0];
        angle2 = v[newstate.side][1];
        angle3 = v[newstate.side][2];

        turret.setPosition(toPosition(turretAngle));
        joint1.setPosition(toPosition(angle1));
        joint2.setPosition(toPosition(angle2));
        joint3.setPosition(toPosition(angle3));

        state = newstate;
        return true;
    }

    public boolean isBusy() {
        return false;
    }

    public void update() {
        var repr =
                state.toString() + "\nturretAngle:" + turretAngle + "\nangle1: " + angle1 + "\nangle2: " + angle2 + "\nangle3:" + angle3;
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
