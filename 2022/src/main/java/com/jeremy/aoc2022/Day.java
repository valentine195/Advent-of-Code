package com.jeremy.aoc2022;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.List;

public abstract class Day {
    public String INPUT;
    public List<String> MATCHES;

    public abstract String runPart1();

    public abstract String runPart2();

    public abstract void run();

    public Day() {

    }

    public Day(String input) {
        setInput(input);
    }

    public void setInput(String input) {
        INPUT = input;
        MATCHES = List.of(INPUT.split("\n"));
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
