package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.jeremy.aoc2022.Day;

enum Comparison {
    EQUAL,
    RIGHT,
    WRONG
}

abstract class BasePacket<T> {
    ListPacket parent;

    abstract T getValue();

    public ListPacket getParent() {
        return parent;
    }

    public void setParent(ListPacket parent) {
        this.parent = parent;
    }

    abstract Comparison compareTo(IntegerPacket packet);

    abstract Comparison compareTo(ListPacket packet);
};

class IntegerPacket extends BasePacket<Integer> {
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void addValue(Integer value) {
        this.value = value;
    }

    Comparison compareTo(IntegerPacket packet) {
        Integer other = packet.getValue();
        if (value == other)
            return Comparison.EQUAL;
        if (value > other)
            return Comparison.WRONG;
        return Comparison.RIGHT;
    }

    Comparison compareTo(ListPacket packet) {
        ListPacket conversion = new ListPacket();
        conversion.addValue(this);
        return conversion.compareTo(packet);
    }
}

class ListPacket extends BasePacket<List<BasePacket>> {
    private List<BasePacket> value = new ArrayList<>();

    public List<BasePacket> getValue() {
        return value;
    }

    public void addValue(BasePacket add) {
        value.add(add);
    }

    Comparison compareTo(IntegerPacket packet) {
        ListPacket conversion = new ListPacket();
        conversion.addValue(packet);
        return compareTo(conversion);
    }

    Comparison compareTo(ListPacket other) {
        for (int i = 0; i < value.size(); i++) {
            if (i >= other.getValue().size())
                return Comparison.WRONG;
            BasePacket currentPacket = value.get(i);
            BasePacket otherPacket = other.getValue().get(i);
            Comparison compare;
            if (otherPacket instanceof IntegerPacket) {
                compare = currentPacket.compareTo((IntegerPacket) otherPacket);
            } else {
                compare = currentPacket.compareTo((ListPacket) otherPacket);
            }

            if (compare == Comparison.EQUAL)
                continue;
            return compare;
        }
        if (other.getValue().size() == value.size())
            return Comparison.EQUAL;
        if (other.getValue().size() > value.size())
            return Comparison.RIGHT;
        return Comparison.WRONG;
    }
}

class Packet {
    ListPacket list = new ListPacket();
    String input;

    public String toString() {
        return input;
    }

    Packet(String input) {
        this.input = input;
        // everything is a nested list... self parse?
        ListPacket current = list;
        List<Character> intList = new ArrayList<>();
        for (char c : input.substring(1, input.length()).toCharArray()) {
            switch (c) {
                case '[': {
                    ListPacket packet = new ListPacket();
                    packet.setParent(current);
                    current.addValue(packet);
                    current = packet;
                    break;
                }
                case ']': {
                    if (intList.size() != 0) {
                        consume(current, intList);
                    }
                    current = current.getParent();
                    break;
                }
                case ',': {
                    if (intList.size() == 0)
                        continue;
                    consume(current, intList);
                    break;
                }
                default: {
                    intList.add(c);
                }
            }
        }
    }

    void consume(ListPacket current, List<Character> intList) {
        IntegerPacket packet = new IntegerPacket();
        packet.addValue(
                Integer.valueOf(intList.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining())));
        intList.clear();
        packet.setParent(current);
        current.addValue(packet);
    }

    Comparison compare(Packet packet) {
        return list.compareTo(packet.list);
    }

}

class PacketGroup {
    List<Packet> packets = new ArrayList<>();

    PacketGroup(String packetGroup) {
        for (String input : packetGroup.split("\n")) {
            Packet packet = new Packet(input);
            packets.add(packet);
        }
    }

    void consume(ListPacket current, List<Character> intList) {
        IntegerPacket packet = new IntegerPacket();
        packet.addValue(
                Integer.valueOf(intList.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining())));
        intList.clear();
        packet.setParent(current);
        current.addValue(packet);
    }

    Comparison compare() {
        return packets.get(0).list.compareTo(packets.get(1).list);
    }
}

public class Day13 extends Day {

    public Day13() {
        super();
    }

    public Day13(String input) {
        super(input);
    }

    public void run() {
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    List<PacketGroup> groups = new ArrayList<>();

    @Override
    public String runPart1() {
        int correctIndices = 0;
        String[] inputGroups = INPUT.split("\n\n");

        for (int i = 0; i < inputGroups.length; i++) {
            String group = inputGroups[i];
            PacketGroup packet = new PacketGroup(group);
            groups.add(packet);

            Comparison compare = packet.compare();
            if (compare == Comparison.RIGHT) {
                correctIndices += i + 1;
            }

        }

        return "" + correctIndices;
    }

    List<Packet> packets = new ArrayList<>();
    List<Packet> decoders = new ArrayList<>();

    @Override
    public String runPart2() {
        for (String line : MATCHES) {
            if (line.trim().length() == 0)
                continue;
            packets.add(new Packet(line));
        }
        decoders.add(new Packet("[[2]]"));
        decoders.add(new Packet("[[6]]"));
        for (Packet decode : decoders) {
            packets.add(decode);
        }

        bubbleSort(packets);

        return "" + ((packets.indexOf(decoders.get(0)) + 1) * (packets.indexOf(decoders.get(1)) + 1));
    }

    void bubbleSort(List<Packet> list) {
        for (int i = 0; i < list.size() - 1; i++)
            for (int j = 0; j < list.size() - i - 1; j++)
                if (list.get(j).compare(list.get(j + 1)) == Comparison.WRONG) {
                    // swap
                    Packet temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
    }

}