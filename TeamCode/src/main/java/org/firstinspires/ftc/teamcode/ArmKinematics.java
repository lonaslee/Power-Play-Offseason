package org.firstinspires.ftc.teamcode;

import static java.lang.Math.*;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Calculates kinematics for an arm mounted on a turret with 2DOF and an
 * additional degree of freedom for the end effector.
 */
public class ArmKinematics {
    private double l1;
    private double l2;
    private double l3;

    public ArmKinematics(double l1, double l2, double l3) {
        this.l1 = l1;
        this.l2 = l2;
        this.l3 = l3;
    }

    public void setLengths(double l1, double l2, double l3) {
        this.l1 = l1;
        this.l2 = l2;
        this.l3 = l3;
    }

    public double @NotNull [] getLengths() {
        return new double[]{l1, l2, l3};
    }

    /**
     * Calculate vertical coordinates of the arm's joints, returning them in an array of array-points.
     */
    public double @NotNull [][] forwardKinematics(double degree1, double degree2, double degree3) {
        double rad1 = toRadians(degree1);
        double rad2 = toRadians(degree2);
        double rad3 = toRadians(degree3);
        double radsum1 = rad1 + rad2;
        double radsum2 = radsum1 + rad3;
        double x1 = l1 * cos(rad1);
        double y1 = l1 * sin(rad1);
        double x2 = l2 * cos(radsum1);
        double y2 = l2 * sin(radsum1);
        double x3 = l3 * cos(radsum2);
        double y3 = l3 * sin(radsum2);
        return new double[][]{{x1, y1}, {x1 + x2, y1 + y2}, {x1 + x2 + x3, y1 + y2 + y3}};
    }

    /**
     * Calculate values that will bring the arm to a given vertical coordinate.
     * Two configurations will be given, for a lefty and righty arm pose. The
     * third angle in every configuration is the angle that makes the end effector
     * parallel to the ground.
     */
    public double @Nullable [][] inverseKinematics(double x, double y) {
        double d = hypot(x, y);
        double b = atan2(y, x);

        double v = (l1 * l1 + l2 * l2 - x * x - y * y) / (2 * l1 * l2);

        if (!(-1 <= v && v <= 1)) return null;

        double m = acos(v);
        double a = acos((x * x + y * y + l1 * l1 - l2 * l2) / (2 * l1 * d));

        double[][] result = new double[2][3];
        result[0][0] = angleWrap(toDegrees(b - a));
        result[0][1] = angleWrap(toDegrees(toRadians(180) - m));
        result[1][0] = angleWrap(toDegrees(b + a));
        result[1][1] = angleWrap(toDegrees(m - toRadians(180)));

        result[0][2] = angleWrap((540 - 180) - result[0][0] - (180 - result[0][1]));
        result[1][2] = angleWrap((540 - 180) - result[1][0] - (180 - result[0][1]));

        return result;
    }

    /**
     * Calculate horizontal position of the arm, given its current total extension
     * length and the degree of the turret it is mounted on.
     */
    public double @NotNull [] forwardCoordinates(double radius, double degreeTurn) {
        double radTurn = toRadians(degreeTurn);
        double z = radius * sin(radTurn) / sin(toRadians(90));
        double x = z / tan(radTurn);
        return new double[]{x, z};
    }

    /**
     * Calculate extension length and turret angle that will bring an arm
     * to a given horizontal coordinate.
     */
    public double @NotNull [] inverseCoordinates(double x, double z) {
        double radius = hypot(x, z);
        double radTurn = atan2(z, x);
        return new double[]{radius, angleWrap(toDegrees(radTurn))};
    }

    public static double angleWrap(double degree) {
        return degree - 360.0 * floor((degree + 180.0) * (1.0 / 360.0));
    }

    @NotNull
    public static String formatArrayPoint(@NonNull double[] arrayPoint) {
        return "(" + roundDecimal(arrayPoint[0]) + ", " + roundDecimal(arrayPoint[1]) + ")";
    }

    public static double roundDecimal(double decimal, int places) {
        int powres = (int) pow(10, places);
        return round(decimal * powres) / (double) powres;
    }

    public static double roundDecimal(double decimal) {
        return roundDecimal(decimal, 2);
    }
}
