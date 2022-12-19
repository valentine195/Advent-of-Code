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
}
