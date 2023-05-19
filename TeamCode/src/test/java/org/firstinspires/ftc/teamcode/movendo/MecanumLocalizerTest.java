package org.firstinspires.ftc.teamcode.movendo;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.DoubleSupplier;

public class MecanumLocalizerTest {
    private double a, b, c, d;

    @Test
    public void test() {
        MecanumLocalizer ms = new MecanumLocalizer(new DoubleSupplier[]{() -> a, () -> b, () -> c, () -> d}, () -> 0);
        for (int i = 0; i < 5000; i++) {
            a = -i;
            b = i;
            c = -i;
            d = i;

            ms.update();
            if (i % 100 == 0)
                System.out.println("curPose=" + ms.getCurrentPose() + " wheels=" + Arrays.toString(
                        new double[]{a, b, c, d}));

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
