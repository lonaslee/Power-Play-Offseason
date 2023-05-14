package org.firstinspires.ftc.teamcode.movendo;


import org.junit.Test;

public class MatrixTest {
    @Test
    public void tostr() {
        var mat = new Matrix(new double[][]{{3, 12, 4}, {5, 6, 8}, {1, 0, 2}});
        var mat2 = new Matrix(new double[][]{{7, 3, 8}, {11, 9, 5}, {6, 8, 4}});
//        var mat = new Mat3x3(new double[][]{{3, 4}, {6, -2}});
//        var mat2 = new Mat3x3(new double[][]{{5, 9, 2}, {0, 7, 8}});
        System.out.println(mat.mul(mat2));
        System.out.println(mat.mul(5));
    }
}
