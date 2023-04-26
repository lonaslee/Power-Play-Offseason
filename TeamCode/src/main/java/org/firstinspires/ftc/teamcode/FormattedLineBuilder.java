package org.firstinspires.ftc.teamcode;


import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class FormattedLineBuilder {
    private final StringBuilder line = new StringBuilder();
    private String curClr = "";
    private boolean spanning = false;

    public FormattedLineBuilder() {
    }

    public FormattedLineBuilder add(Object str) {
        line.append(str.toString());
        return this;
    }

    public FormattedLineBuilder clr(String clr) {
        if (spanning) end();
        spanning = true;
        curClr = clr;
        add(String.format("<span style=\"color:%s\">", clr));
        return this;
    }

    public FormattedLineBuilder red() {
        return clr("red");
    }

    public FormattedLineBuilder yellow() {
        return clr("yellow");
    }

    public FormattedLineBuilder blue() {
        return clr("blue");
    }

    public FormattedLineBuilder orange() {
        return clr("orange");
    }

    public FormattedLineBuilder green() {
        return clr("blue");
    }

    public FormattedLineBuilder violet() {
        return clr("violet");
    }

    public FormattedLineBuilder end() {
        spanning = false;
        add("</span>");
        return this;
    }

    public FormattedLineBuilder addData(String label, Object obj, String labelClr, String objClr) {
        var prevClr = curClr;
        clr(labelClr);
        add(label);
        add(": ");
        clr(objClr);
        add(obj);
        clr(prevClr);
        return this;
    }

    public FormattedLineBuilder repeat(String str, int times) {
        for (int i = 0; i < times; i++) line.append(str);
        return this;
    }

    public FormattedLineBuilder nl() {
        return add("\n");
    }

    @NonNull
    @Override
    public String toString() {
        if (spanning) end();
        return line.toString();
    }

    public static Telemetry initTelemetry(Telemetry telemetry) {
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);
        telemetry.update();
        return telemetry;
    }
}
