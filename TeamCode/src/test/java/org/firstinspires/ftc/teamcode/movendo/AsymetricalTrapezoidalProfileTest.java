package org.firstinspires.ftc.teamcode.movendo;

import static org.junit.Assert.*;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.junit.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

public class AsymetricalTrapezoidalProfileTest {

    @Test
    public void trapezoid() {
        AsymetricalTrapezoidalProfile p = new AsymetricalTrapezoidalProfile(0, 100, 8, 20, 5);
        ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.SECONDS);
//        if (true) return;
        while (true) {
            final double t = timer.time();
            if (p.isFinished(t)) break;

//            System.out.println(Arrays.toString(Arrays.stream(p.at(t)).map(n -> Double.parseDouble(df.format(n))).toArray()));
            if (df.format(t).charAt(df.format(t).length() - 1) == '1') {
                System.out.println("(" + t + ", " + p.at(t)[2] + ")");
                System.out.println("(" + t + ", " + p.at(t)[1] + ")");
                System.out.println("(" + t + ", " + p.at(t)[0] + ")");
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static DecimalFormat df = new DecimalFormat(".##");

    static {
        df.setRoundingMode(RoundingMode.HALF_UP);
    }
}