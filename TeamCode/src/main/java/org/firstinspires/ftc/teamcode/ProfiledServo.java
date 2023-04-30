package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Arrays;

public class ProfiledServo {
    public final ServoImplEx servo;

    private final ElapsedTime timer = new ElapsedTime();
    @NonNull private TrapezodialProfile profile;

    private double target;
    private double defaultVel;
    private double defaultAccel;

    public ProfiledServo(
            @NonNull Servo servo, double initPos, double defaultVel, double defaultAccel
    ) {
        this.servo = (ServoImplEx) servo;
        this.servo.setPwmRange(new PwmControl.PwmRange(500, 2500));

        this.defaultVel = defaultVel;
        this.defaultAccel = defaultAccel;

        target = initPos;
        servo.setPosition(initPos);
        profile = new TrapezodialProfile(initPos, initPos, 0, 0);
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double pos, double mV, double mA) {
        if (pos == target) return;
        target = pos;
        profile = new TrapezodialProfile(getCurrentPosition(), target, mV, mA);
        timer.reset();
    }

    public void setTarget(double pos) {
        setTarget(pos, defaultVel, defaultAccel);
    }

    public double getCurrentPosition() {
        return profile.at(timer.seconds())[2];
    }

    public void update() {
        final var res = profile.at(timer.seconds());
        System.out.println("avx = " + Arrays.toString(res) + " ; at time " + timer.seconds());
        if (servo.getPosition() != res[2]) {
            servo.setPosition(res[2]);
        }
    }

    public double getDefaultVel() {
        return defaultVel;
    }

    public void setDefaultVel(double defaultVel) {
        this.defaultVel = defaultVel;
    }

    public double getDefaultAccel() {
        return defaultAccel;
    }

    public void setDefaultAccel(double defaultAccel) {
        this.defaultAccel = defaultAccel;
    }
}
