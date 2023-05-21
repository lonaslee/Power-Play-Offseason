package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.movendo.TrapezoidalProfile;

/**
 * Class that enables full control of a servo's acceleration and velocity at all
 * times through a motion profile.
 */
public class ProfiledServo {
    public final ServoImplEx servo;

    private final ElapsedTime timer = new ElapsedTime();
    @NonNull
    private TrapezoidalProfile profile;

    private double position;
    private double defaultAccel;
    private double defaultVel;
    private double defaultDecel;

    public ProfiledServo(@NonNull Servo servo, double initPos, double defaultAccel, double defaultVel, double defaultDecel) {
        this.servo = (ServoImplEx) servo;
        this.servo.setPwmRange(new PwmControl.PwmRange(500, 2500));

        this.defaultAccel = defaultAccel;
        this.defaultVel = defaultVel;
        this.defaultDecel = defaultDecel;

        position = initPos;
        profile = new TrapezoidalProfile(0, initPos, defaultVel, defaultAccel);
    }

    public ProfiledServo(@NonNull Servo servo, double initPos, double defaultAccel, double defaultVel) {
        this(servo, initPos, defaultAccel, defaultVel, defaultAccel);
    }

    public ProfiledServo(@NonNull Servo servo, double initPos) {
        this(servo, initPos, 0.5, 1.5, 0.5);
    }

    public double getPosition() {
        return position;
    }

    /**
     * Go to pos with different acceleration and deceleration.
     */
    public void setTarget(double pos, double mA, double mV, double mD) {
        if (pos == position) return;
        position = pos;
        profile = new TrapezoidalProfile(getCurrentPosition(), position, mA, mV, mD);
        timer.reset();
    }

    /**
     * Go to pos using a symmetrical profile.
     */
    public void setTarget(double pos, double mA, double mV) {
        setTarget(pos, mA, mV, mA);
    }

    /**
     * Go to pos using default acceleration/velocity/deceleration.
     */
    public void setPosition(double pos) {
        setTarget(pos, defaultAccel, defaultVel, defaultDecel);
    }

    /**
     * Returns the current position of the active motion profile.
     */
    public double getCurrentPosition() {
        return profile.at(timer.seconds())[2];
    }

    public void update() {
        final var res = profile.at(timer.seconds());
        if (servo.getPosition() != res[2]) servo.setPosition(res[2]);
    }

    public double getDefaultAccel() {
        return defaultAccel;
    }

    public void setDefaultAccel(double defaultAccel) {
        this.defaultAccel = defaultAccel;
    }

    public double getDefaultVel() {
        return defaultVel;
    }

    public void setDefaultVel(double defaultVel) {
        this.defaultVel = defaultVel;
    }

    public double getDefaultDecel() {
        return defaultDecel;
    }

    public void setDefaultDecel(double defaultDecel) {
        this.defaultDecel = defaultDecel;
    }
}
