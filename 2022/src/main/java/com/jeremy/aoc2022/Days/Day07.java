package com.jeremy.aoc2022.Days;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import com.jeremy.aoc2022.Day;

import lombok.Getter;

class Regexes {
    static String MOVEMENT = "\\$ cd (?<dir>.+)";
    static String FILE = "(?<size>\\d+) (?<name>.+)";
    static String DIRECTORY = "dir (?<name>\\w+)";
}

abstract class Item {
    @Getter
    String name;

    @Getter
    Folder parent;

    abstract int getSize();
}

class File extends Item {
    @Getter
    int size;

    @Getter
    String name;

    @Getter
    Folder parent;

    File(String name, int size, Folder parent) {
        this.size = size;
        this.name = name;
    }
}

@Getter
class Folder extends Item {

    @Getter
    String name;

    Folder(String name) {
        this.name = name;
    }

    Folder(String name, Folder parent) {
        this.name = name;
        this.parent = parent;
    }

    boolean isTop() {
        return Objects.isNull(parent);
    }

    void addChild(Item child) {
        children.add(child);
    }

    List<Item> children = new ArrayList<>();

    int getSize() {
        return children.stream().map(c -> c.getSize()).mapToInt(c -> c).sum();
    }

    Folder getChildFolder(String name) {
        for (Item child : children) {
            if (child instanceof File)
                continue;
            if (child.getName().equals(name)) {
                return (Folder) child;
            }
        }
        throw new Error("No child by that name");
    }
}

public class Day07 extends Day {
    public Day07() {
        super();
    }

    public Day07(String input) {
        super(input);
    }

    Folder top = new Folder("/");
    Pattern FOLDER = Pattern.compile(Regexes.DIRECTORY);
    Pattern FILE = Pattern.compile(Regexes.FILE);
    Pattern UP = Pattern.compile(Regexes.MOVEMENT);

    List<Folder> allDirectories = new ArrayList<>();

    public void buildTree() {
        Folder current = top;
        for (String line : MATCHES) {
            line = line.trim();
            if (line.matches(Regexes.MOVEMENT)) {
                if (line.equals("$ cd /")) {
                    current = top;
                } else if (line.equals("$ cd ..")) {
                    current = current.getParent();
                } else {
                    current = current.getChildFolder(line.split(" ")[2]);
                }
            } else if (line.matches(Regexes.DIRECTORY)) {
                Folder folder = new Folder(line.split(" ")[1], current);
                current.addChild(folder);
                allDirectories.add(folder);
            } else if (line.matches(Regexes.FILE)) {
                String[] split = line.split(" ");
                current.addChild(new File(split[1], Integer.parseInt(split[0]), current));
            }
        }
    }

    public void run() {
        buildTree();
        System.out.println(runPart1());
        System.out.println(runPart2());
    }

    public String runPart1() {
        Integer larger = allDirectories.stream().map(c -> c.getSize()).filter(c -> c <= 100000).mapToInt(c -> c).sum();
        System.out.println(larger);
        return larger.toString();

    }

    int MAX = 70000000;
    int MIN = 30000000;

    public String runPart2() {
        Integer available = MAX - top.getSize();
        Integer required = MIN - available;
        return "" + (allDirectories.stream().mapToInt(c -> c.getSize()).filter(c -> c > required).boxed().sorted()
                .toList().get(0));

    }
}
