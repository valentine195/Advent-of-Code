package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jeremy.aoc2022.Day;

public class Day20 extends Day {
    public Day20(String input) {
        super(input);
    }

    public Day20() {
        super();
    }

    class Value {
        long val;

        Value(long val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return Long.toString(val);
        }
    }

    long execute(long key, int times) {
        List<Value> values = MATCHES.stream().map(i -> new Value(Long.parseLong(i) * key)).collect(Collectors.toList());
        List<Value> order = new ArrayList<>(values);

        for (int i = 0; i < times; i++) {
            for (Value next : order) {
                int index = values.indexOf(next);
                values.remove(index);
                values.add(Math.floorMod(next.val + index, values.size()), next);
            }
        }

        long grove = 0;
        Optional<Value> zero = values.stream().filter(n -> n.val == 0).findFirst();
        if (!zero.isPresent()) {
            throw new Error("Could not find zero!");
        }
        int zeroIndex = values.indexOf(zero.get());
        for (int i = 1; i <= 3; i++) {
            grove += values.get((zeroIndex + 1000 * i) % values.size()).val;
        }
        return grove;
    }

    @Override
    public String runPart1() {
        return "" + execute(1, 1);
    }

    @Override
    public String runPart2() {
        return "" + execute(811589153, 10);
    }
}
