package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jeremy.aoc2022.Day;

public class Day25 extends Day {

    public Day25(String input) {
        super(input);
    }

    public Day25() {
        super();
    }

    long getDigit(char digit) {
        return switch (digit) {
            case '2' -> 2;
            case '1' -> 1;
            case '0' -> 0;
            case '-' -> -1;
            case '=' -> -2;
            default -> throw new IllegalArgumentException();
        };
    }

    String reverseDigit(int digit) {
        return switch (digit) {
            case 2 -> "2";
            case 1 -> "1";
            case 0 -> "0";
            case -1 -> "-";
            case -2 -> "=";
            default -> throw new IllegalArgumentException();
        };
    }

    long getDecimal(String snafu) {
        long decimal = 0;

        for (int i = 0; i < snafu.length(); i++) {
            long res = (long) Math.pow(5, (snafu.length() - 1 - i)) * getDigit(snafu.charAt(i));
            decimal += res;
        }
        return decimal;
    }

    String getSnafu(long decimal) {

        String snafu = "";
        while (true) {
            if (decimal <= 0)
                break;
            snafu = reverseDigit((int) ((decimal + 2) % 5) - 2) + snafu;
            decimal = (long) Math.floor((decimal + 2) / 5);
        }
        return snafu;
    }

    @Override
    public String runPart1() {
        long sum = 0;
        for (String line : MATCHES) {
            long decimal = getDecimal(line);
            sum += decimal;
        }
        return getSnafu(sum);
    }

    @Override
    public String runPart2() {
        // TODO Auto-generated method stub
        return null;
    }
}
