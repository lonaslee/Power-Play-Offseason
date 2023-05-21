package org.firstinspires.ftc.teamcode.movendo;


import android.renderscript.Matrix3f;

import org.junit.Test;

public class MatrixTest {
    @Test
    public void tostr() {
        var mat = new Matrix(new double[][]{{3, 12, 4}, {5, 6, 8}, {1, 0, 2}});
        var mat2 = new Matrix(new double[][]{{7, 3, 8}, {11, 9, 5}, {6, 8, 4}});
        System.out.println(mat.mul(mat2));
        System.out.println(mat.mul(5));
    }

    @Test
    public void op() {
        Matrix mat1 = new Matrix(new double[][]{
                {1, 2, 3},
                {1, 2, 3}
        });
        Matrix mat2 = new Matrix(new double[][]{
                {2, 2, 3},
                {4, 5, 2}
        });

        System.out.println(mat1.add(mat2));
        System.out.println(mat1.sub(mat2));
    }

    @Test
    public void cloneTest() {
        var mat = new Matrix(new double[][]{{1, 2}, {2, 1}});
        var mat2 = mat.copy();
        System.out.println(mat);
        System.out.println(mat2);

        mat2.set(0, 1, 5);
        System.out.println(mat);
        System.out.println(mat2);
    }
}
