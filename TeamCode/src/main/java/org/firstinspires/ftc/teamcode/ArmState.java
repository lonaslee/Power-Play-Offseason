package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.ArmKinematics.roundDecimal;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;


import java.util.Objects;

/**
 * Represents a point (x, y, z) within the 3d grid of the arm's range
 * of motion. The side corresponds to a lefty-righty/upper-lower joint
 * configuration (0 or 1). The claw angle is the angle of the end effector
 * relative to the ground.
 */
@Config
public class ArmState {
    public final double x;
    public final double y;
    public final double z;
    public final int side;
    public final double endEffectorAngle;

    public final double angle1;
    public final double angle2;
    public final double angle3;
    public final double angle4;

    public final boolean reachable;

    public static double L1 = 10;
    public static double L2 = 10;
    public static double L3 = 10;
    private static final ArmKinematics kinematics = new ArmKinematics(L1, L2, L3);

    public ArmState(double x, double y, double z, int side, double endEffectorAngle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.side = side;
        this.endEffectorAngle = endEffectorAngle;

        var h = kinematics.inverseCoordinates(x, z);
        var v = kinematics.inverseKinematics(h[0], y);

        reachable = v != null;
        if (!reachable) {
            angle1 = angle2 = angle3 = angle4 = 0;
        } else {
            angle1 = h[1];
            angle2 = v[side][0];
            angle3 = v[side][1];
            angle4 = v[side][2];
        }
    }

    public ArmState(double x, double y, double z, double endEffectorAngle) {
        this(x, y, z, 0, endEffectorAngle);
    }

    public ArmState(double x, double y, double z) {
        this(x, y, z, 0, 0);
    }

    @NonNull
    public static ArmState fromAngles(double angle1, double angle2, double angle3, double angle4) {
        var v = kinematics.forwardKinematics(angle2, angle3, angle4);
        var h = kinematics.forwardCoordinates(v[2][0], angle1);
        return new ArmState(h[0], v[2][1], h[1]);
    }

    @NonNull
    public ArmState delta(double dx, double dy, double dz, double dEndEffectorAngle) {
        return new ArmState(x + dx, y + dy, z + dz, side, endEffectorAngle + dEndEffectorAngle);
    }

    @NonNull
    public ArmState delta(double dx, double dy, double dz) {
        return delta(dx, dy, dz, 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, side, endEffectorAngle);
    }

    public boolean equals(@NonNull ArmState other) {
        return other.hashCode() == hashCode();
    }

    @NonNull
    @Override
    protected final ArmState clone() {
        return new ArmState(x, y, z, side, endEffectorAngle);
    }

    @Override
    @NonNull
    public String toString() {
        return "ArmState{ " + "(" + roundDecimal(x) + ", " + roundDecimal(y) + ", " + roundDecimal(z) + ") " + ", side=" + side + ", endEffectorAngle=" + endEffectorAngle + ", angle1=" + roundDecimal(
                angle1) + ", angle2=" + roundDecimal(angle2) + ", angle3=" + roundDecimal(angle3) + ", angle4=" + roundDecimal(
                angle4) + ", reachable=" + reachable + " }";
    }
}
