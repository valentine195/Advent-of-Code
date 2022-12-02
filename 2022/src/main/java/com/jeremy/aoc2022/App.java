package com.jeremy.aoc2022;

import java.util.Calendar;

import com.jeremy.aoc2022.Day1.Day1;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] arg) {
        int day;
        if (arg.equals(null) || arg.length == 0 || !isNumeric(arg[0])) {
            day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        } else {
            day = Integer.parseInt(arg[0]);
        }
        String input = downloadDay(day);
        new Day1().run(input);
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
                            "session=53616c7465645f5ff89cd22db7326fc559e8a6ee6f9892e0b54ef95c3b6e21269cfc6ec698690b8d58177b67d9464d25c957c746812bd7b37c7d1bf3ea778d52")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            return null;
        }
    }
}
