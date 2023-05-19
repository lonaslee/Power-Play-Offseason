package org.firstinspires.ftc.teamcode.movendo;

import org.junit.Test;


public class KalmanFilterTest {
    @Test
    public void test() {
        KalmanFilter f = new KalmanFilter();

        for (int i = 0; i < 10; i++) {
            System.out.println(f.estimate(175 + i, 180 + (i / 2.0)));
        }
    }
}
