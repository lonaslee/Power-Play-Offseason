package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.jetbrains.annotations.Contract;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.function.Consumer;

/**
 * Builds a telemetry line with colored text and other elements. The telemetry object needs
 * to be initialized with the {@link FormattedLineBuilder#initTelemetry(Telemetry)} method.
 */
public class FormattedLineBuilder {
    private final StringBuilder line = new StringBuilder();

    private String curClr = "";
    private boolean spanning = false;

    private String[] colors = null;
    private Consumer<String[]> adder = null;

    public FormattedLineBuilder() {
    }

    /**
     * Add an object's string representation to the line being built, applying html
     * escapes.
     */
    public FormattedLineBuilder add(@NonNull Object obj) {
        line.append(obj.toString()
                .replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll(" ", "&nbsp;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&apos;"));
        return this;
    }

    /**
     * Switch the current text color to a new color.
     *
     * @param clr Name or hex code of the color. Some of them don't work on phone
     *            displays.
     * @return this
     */
    public FormattedLineBuilder clr(@NonNull String clr) {
        String newclr;
        try {
            final var hex = clr.replaceAll("0x", "").replaceAll("#", "");
            Long.parseLong(hex, 16);
            newclr = "#" + hex;
        } catch (NumberFormatException e) {
            newclr = clr;
        }

        if (colors != null) {
            int i;
            for (i = 0; i < colors.length; i++) {
                if (colors[i] == null) {
                    colors[i] = clr;
                    break;
                }
            }

            if (i == colors.length - 1) {
                final var ref = colors;
                colors = null;
                adder.accept(ref);
            }
        } else {
            if (spanning) end();
            spanning = true;
            curClr = newclr;
            rawAdd(String.format("<span style=\"color:%s\">", curClr));
        }
        return this;
    }

    /**
     * End any current formatting, if there is any. The building methods will
     * automatically take care of this.
     */
    public FormattedLineBuilder end() {
        if (spanning) {
            spanning = false;
            rawAdd("</span>");
        }
        return this;
    }

    /**
     * Add formatted data, in the form of "label : data."
     * Follow the call to this building method with two color methods, which will be
     * used for the label and the data, respectively.
     * <pre>
     * new {FormattedLineBuilder}()
     *   .startData("label", "data")
     *   .{@link FormattedLineBuilder#red}()
     *   .{@link FormattedLineBuilder#clr}("00FF00")</pre>
     */
    public FormattedLineBuilder startData(String label, Object data) {
        colors = new String[2];
        final var prevClr = curClr;
        adder = (clrs) -> {
            for (int i = 0; i < clrs.length; i++)
                if (clrs[i] == null) clrs[i] = "white";
            clr(clrs[0]);
            add(label);
            black();
            add(" : ");
            clr(clrs[1]);
            add(data.toString());
            clr(prevClr);
        };
        return this;
    }

    /**
     * Add a slider, ranging from the given min and max, with the bar at the given current position.
     * Follow the call to this building method with three color methods, which will be used for
     * the min/max labels, the bar, and the slider range, respectively.
     * <pre>
     * new FormattedLineBuilder()
     *   .startSlider(0, 100, 30)
     *   .{@link FormattedLineBuilder#blue}()
     *   .{@link FormattedLineBuilder#clr}("00FFFF")
     *   .{@link FormattedLineBuilder#red}()</pre>
     */
    public FormattedLineBuilder startSlider(double min, double max, double cur) {
        colors = new String[3];
        final var prevClr = curClr;

        final var df = new DecimalFormat(".#");
        df.setRoundingMode(RoundingMode.HALF_UP);
        final var idx = Integer.parseInt(String.valueOf(df.format((cur / (max + 1e-6 - min)) * 2)
                .charAt(1)));

        adder = (clrs) -> {
            for (int i = 0; i < clrs.length; i++)
                if (clrs[i] == null) clrs[i] = "white";
            clr(clrs[0]);
            add(min);
            clr(clrs[2]);
            add(" [");
            repeat("━", idx);
            clr(clrs[1]);
            add("█");
            clr(clrs[2]);
            repeat("━", 20 - idx - 1);
            add("] ");
            clr(clrs[0]);
            add(max);
            clr(clrs[1]);
            add(" | ");
            add(cur);
            add(" ");
            clr(prevClr);
        };
        return this;
    }

    /**
     * Repeat the given string a number of times.
     */
    public FormattedLineBuilder repeat(String str, int times) {
        for (int i = 0; i < times; i++)
            add(str);
        return this;
    }

    /**
     * Add a newline.
     */
    public FormattedLineBuilder nl() {
        return add("\n");
    }

    /**
     * Add without escaping html characters.
     */
    public FormattedLineBuilder rawAdd(@NonNull Object obj) {
        line.append(obj.toString());
        return this;
    }

    /* --------------- colors --------------- */

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
        return clr("EA5D00");
    }

    public FormattedLineBuilder green() {
        return clr("green");
    }

    public FormattedLineBuilder violet() {
        return clr("violet");
    }

    public FormattedLineBuilder lime() {
        return clr("69FF00");
    }

    public FormattedLineBuilder cyan() {
        return clr("00E5E5");
    }

    public FormattedLineBuilder purple() {
        return clr("5A00E2");
    }

    public FormattedLineBuilder magenta() {
        return clr("BE00FF");
    }

    public FormattedLineBuilder pink() {
        return clr("FF3ADC");
    }

    public FormattedLineBuilder lightGray() {
        return clr("808080");
    }

    public FormattedLineBuilder darkGray() {
        return clr("404040");
    }

    public FormattedLineBuilder black() {
        return clr("000000");
    }

    public FormattedLineBuilder white() {
        return clr("FFFFFF");
    }

    /**
     * Get the formatted line that was built.
     */
    @NonNull
    @Override
    public String toString() {
        if (spanning) end();
        return line.toString();
    }

    /**
     * Set up a telemetry object to work with html formatting. This method needs to be called
     * for formatting to work.
     */
    @NonNull
    @Contract("_ -> param1")
    public static Telemetry initTelemetry(@NonNull Telemetry telemetry) {
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.HTML);
        telemetry.update();
        return telemetry;
    }
}
