package org.firstinspires.ftc.teamcode.movendo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;


public class Pose {
    public final double x;
    public final double y;
    public final double h;

    public Pose(double x, double y, double h) {
        this.x = x;
        this.y = y;
        this.h = AngleUnit.normalizeRadians(h);
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Pose) && (o.hashCode() == hashCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, h);
    }

    @Override
    public String toString() {
        return "(" + df.format(x) + ", " + df.format(y) + ", " + df.format(Math.toDegrees(h)) + ")";
    }

    private static final DecimalFormat df;

    static {
        df = new DecimalFormat(".##");
        df.setRoundingMode(RoundingMode.HALF_UP);
    }
}
