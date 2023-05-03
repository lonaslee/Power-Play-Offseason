package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.jetbrains.annotations.Contract;

import java.util.function.Consumer;

/**
 * Builds a telemetry line with colored text and other elements. The telemetry object needs
 * to be initialized with the {@link FormattedLineBuilder#initTelemetry(Telemetry)} method.
 */
public class FormattedLineBuilder {
    private final StringBuilder line = new StringBuilder();

    private String curClr = "";
    private boolean spanning = false;

    @Nullable
    private String[] colors = null;
    @Nullable
    private Consumer<String[]> adder = null;

    public FormattedLineBuilder() {
    }

    /**
     * Add an object's string representation to the line being built, applying html
     * escapes.
     */
    public FormattedLineBuilder add(@NonNull Object obj) {
        if (adder != null)
            throw new FormattedLineBuilderException("Adding elements during entry type.\n" + line);

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
     * Add without escaping html characters.
     */
    public FormattedLineBuilder rawAdd(@NonNull Object obj) {
        if (adder != null)
            throw new FormattedLineBuilderException("Adding raw elements during entry type.\n" + line);

        line.append(obj);
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
                if (adder == null)
                    throw new FormattedLineBuilderException("Adder is null for some reason.\n" + line);
                final var addr = adder;
                adder = null;
                addr.accept(ref);
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
     * new FormattedLineBuilder()
     *   .startData("label", "data")
     *   .{@link FormattedLineBuilder#red}()
     *   .{@link FormattedLineBuilder#clr}("00FF00")</pre>
     */
    public FormattedLineBuilder startData(String label, Object data) {
        if (adder != null)
            throw new FormattedLineBuilderException("Starting data entry during another entry type.\n" + "adder : " + adder + "\n" + line);

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
        if (adder != null)
            throw new FormattedLineBuilderException(
                    "Starting slider entry during another entry type.\n" + line);

        colors = new String[3];
        final var prevClr = curClr;

        final var percent = cur / (max + 1e-6 - min);
        final var idx = (int) Math.round(20 * percent);

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
     * Similar to {@link FormattedLineBuilder#startSlider(double, double, double)}, except
     * it is a progress bar. Follow calls to this method with two color methods, which will be
     * used for completed and uncompleted parts of the bar, respectively.
     */
    public FormattedLineBuilder startProgressBar(double min, double max, double cur) {
        if (adder != null) throw new FormattedLineBuilderException(
                "Starting progress bar entry during another entry type.\n" + line);

        colors = new String[2];
        final var prevClr = curClr;

        final var percent = cur / (max + 1e-6 - min);
        final var idx = (int) Math.round(20 * percent);

        adder = (clrs) -> {
            for (int i = 0; i < clrs.length; i++)
                if (clrs[i] == null) clrs[i] = "white";
            clr(clrs[1]);
            add("[");
            clr(clrs[0]);
            repeat("█", idx + 1);
            clr(clrs[1]);
            repeat("━", 20 - idx - 1);
            add("] ");
            clr(clrs[0]);
            add((int) (percent * 100));
            add("% ");
            clr(prevClr);
        };
        return this;
    }

    /**
     * Add a spinner, automatically determining its current phase based on
     * {@link System#currentTimeMillis()}.
     */
    public FormattedLineBuilder spinner(
            @NonNull String[] phases, int phaseLengthMillis, int offset
    ) {
        final var idx =
                (int) (((System.currentTimeMillis()) / phaseLengthMillis + offset) % phases.length);
        add(phases[idx]);
        return this;
    }

    public FormattedLineBuilder spinner(int phaseLengthMillis, int offset) {
        return spinner(
                new String[]{"⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"},
                phaseLengthMillis,
                offset
        );
    }

    /**
     * Default spinner with phase length of 100ms and offset of 0.
     */
    public FormattedLineBuilder spinner() {
        return spinner(100, 0);
    }

    public FormattedLineBuilder spinnerBig(int phaseLengthMillis, int offset) {
        return spinner(
                new String[]{"⡀⠀", "⠄⠀", "⢂⠀", "⡂⠀", "⠅⠀", "⢃⠀", "⡃⠀", "⠍⠀", "⢋⠀", "⡋⠀", "⠍⠁", "⢋⠁", "⡋⠁", "⠍⠉", "⠋⠉", "⠋⠉", "⠉⠙", "⠉⠙", "⠉⠩", "⠈⢙", "⠈⡙", "⢈⠩", "⡀⢙", "⠄⡙", "⢂⠩", "⡂⢘", "⠅⡘", "⢃⠨", "⡃⢐", "⠍⡐", "⢋⠠", "⡋⢀", "⠍⡁", "⢋⠁", "⡋⠁", "⠍⠉", "⠋⠉", "⠋⠉", "⠉⠙", "⠉⠙", "⠉⠩", "⠈⢙", "⠈⡙", "⠈⠩", "⠀⢙", "⠀⡙", "⠀⠩", "⠀⢘", "⠀⡘", "⠀⠨", "⠀⢐", "⠀⡐", "⠀⠠", "⠀⢀", "⠀⡀"},
                phaseLengthMillis,
                offset
        );
    }

    public FormattedLineBuilder spinnerBig() {
        return spinnerBig(100, 0);
    }

    public FormattedLineBuilder spinnerLine(int phaseLengthMillis, int offset) {
        return spinner(new String[]{"-", "\\", "|", "/"}, phaseLengthMillis, offset);
    }

    public FormattedLineBuilder spinnerLine() {
        return spinnerLine(100, 0);
    }

    public FormattedLineBuilder spinnerNoise(int phaseLengthMillis, int offset) {
        return spinner(new String[]{"▓", "▒", "░"}, phaseLengthMillis, offset);
    }

    public FormattedLineBuilder spinnerNoise() {
        return spinnerNoise(100, 0);
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

    public static class FormattedLineBuilderException extends RuntimeException {
        public FormattedLineBuilderException(String msg) {
            super(msg);
        }
    }
}
