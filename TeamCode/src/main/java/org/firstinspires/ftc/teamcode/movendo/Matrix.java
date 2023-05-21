package org.firstinspires.ftc.teamcode.movendo;


import java.util.Arrays;
import java.util.Locale;


public class Matrix implements Cloneable {
    private final double[][] vals;
    public final int numRows;
    public final int numCols;

    public Matrix(double[][] vals) {
        this.vals = vals;
        numRows = vals.length;
        numCols = vals[0].length;
    }

    public double get(int x, int y) {
        return vals[y][x];
    }

    public void set(int x, int y, double val) {
        vals[y][x] = val;
    }

    public Matrix mul(Matrix other) {
        if (numCols != other.numRows)
            throw new IllegalArgumentException("Matrix multiplication with incompatible matrices: " + other.toOrderString() + " & " + toOrderString());

        Matrix res = new Matrix(new double[numRows][other.numCols]);
        for (int y = 0; y < numRows; y++) {
            for (int x = 0; x < other.numCols; x++) {
                double val = 0;
                for (int i = 0; i < numRows; i++) {
                    val += get(i, y) * other.get(x, i);
                }
                res.set(x, y, val);
            }
        }
        return res;
    }

    public Matrix mul(double scalar) {
        Matrix res = clone();
        for (int y = 0; y < res.numRows; y++)
            for (int x = 0; x < res.numCols; x++)
                res.set(x, y, res.get(x, y) * scalar);
        return res;
    }

    public Matrix add(Matrix other) {
        if (numCols != other.numCols || numRows != other.numRows)
            throw new IllegalArgumentException("Matrix addition with incompatible matrices: " + other.toOrderString() + " & " + toOrderString());

        final double[][] newvals = vals.clone();
        for (int n = 0; n < newvals.length; n++)
            for (int m = 0; m < newvals[n].length; m++)
                newvals[n][m] += other.vals[n][m];

        return new Matrix(newvals);
    }

    public Matrix sub(Matrix other) {
        return add(other.neg());
    }

    public Matrix neg() {
        return mul(-1);
    }

    public String toOrderString() {
        return numRows + " x " + numCols;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < numRows; y++) {
            for (int x = 0; x < numCols; x++) {
                sb.append(String.format(Locale.ENGLISH, "%.2f\t", get(x, y)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Matrix && o.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(vals);
        result = 31 * result + numRows;
        result = 31 * result + numCols;
        return result;
    }

    @Override
    public Matrix clone() {
        return new Matrix(vals.clone());
    }
}
