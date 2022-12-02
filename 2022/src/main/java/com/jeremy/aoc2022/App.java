package com.jeremy.aoc2022;

import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Hello world!
 *
 */
public class App {
    static Runner runner = new Runner();

    public static void main(String[] arg) {
        int day;
        if (arg.equals(null) || arg.length == 0 || !isNumeric(arg[0])) {
            day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        } else {
            day = Integer.parseInt(arg[0]);
        }
        String input = downloadDay(day);
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

    public static String downloadDay(int day) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://adventofcode.com/2022/day/" + day + "/input")
                    .addHeader("Cookie",
                            "session=" + System.getenv("SESSION_COOKIE"))
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            return null;
        }
    }
}
