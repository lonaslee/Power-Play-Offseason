package org.firstinspires.ftc.teamcode.movendo;

import org.junit.Test;


public class PoseTest {

    @Test
    public void test() {
        Pose a = new Pose(0, 0, 0);
        Pose b = new Pose(1, 1, 1);
        assert !a.equals(b);

        Pose c = new Pose(1, 1, 1);
        assert b.equals(c);
        System.out.println("PoseTest.test");
    }
}
