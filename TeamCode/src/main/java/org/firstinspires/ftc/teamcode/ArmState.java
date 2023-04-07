package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.ArmKinematics.roundDecimal;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a point (x, y, z) within the 3d grid of the arm's range
 * of motion. The side corresponds to a lefty-righty/upper-lower joint
 * configuration (0 or 1). The claw angle is the angle of the end effector
 * relative to the ground.
 */
public class ArmState {
    public final double x;
    public final double y;
    public final double z;
    public final int side;
    public final double endEffectorAngle;

    public ArmState(double x, double y, double z, int side, double endEffectorAngle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.side = side;
        this.endEffectorAngle = endEffectorAngle;
    }

    public ArmState(double x, double y, double z, double endEffectorAngle) {
        this(x, y, z, 0, endEffectorAngle);
    }

    public ArmState(double x, double y, double z) {
        this(x, y, z, 0, 0);
    }

    @NotNull
    public ArmState delta(double dx, double dy, double dz, double dEndEffectorAngle) {
        return new ArmState(x + dx, y + dy, z + dz, side, endEffectorAngle + dEndEffectorAngle);
    }

    @NotNull
    public ArmState delta(double dx, double dy, double dz) {
        return delta(dx, dy, dz, 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, side, endEffectorAngle);
    }

    public boolean equals(ArmState other) {
        return other.hashCode() == hashCode();
    }

    @Override
    @NotNull
    public String toString() {
        return "ArmState [ (" + roundDecimal(x, 2) + ", " + roundDecimal(y, 2) + ", " + roundDecimal(z, 2) + "), side=" + side + ", endEffectorAngle=" + endEffectorAngle + " ]";
    }
}
