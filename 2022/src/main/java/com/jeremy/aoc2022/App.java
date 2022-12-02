package com.jeremy.aoc2022;

import java.util.Calendar;

/**
 * Hello world!
 *
 */
public class App {
    static Runner runner = new Runner();
    static Loader loader = new Loader();

    public static void main(String[] arg) {
        int day;
        if (arg.equals(null) || arg.length == 0 || !isNumeric(arg[0])) {
            day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        } else {
            day = Integer.parseInt(arg[0]);
        }
        String input = loader.getDataForDay(day);
        runner.runDay(day, input);
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
