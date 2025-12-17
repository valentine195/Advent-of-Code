package main

import (
	"fmt"
	"os"
	"slices"
	"strconv"
	"strings"
)

func main() {
	// Read input from a file named "input.txt" in the same directory
	data, err := os.ReadFile("input.txt")
	if err != nil {
		panic(err)
	}
	input := string(data)

	part1Result := part1(input)
	part2Result := part2(input)

	fmt.Printf("Part 1: %d\n", part1Result)
	fmt.Printf("Part 2: %d\n", part2Result)
}

type interval struct {
	start int64
	end   int64
}

func part1(input string) int {
	// Normalize line endings and split blocks robustly (handle CRLF and missing exact \n\n)
	input = strings.ReplaceAll(input, "\r\n", "\n")
	split := strings.Split(input, "\n\n")

	ranges, ingredients := split[0], split[1]

	// Parse ranges
	var intervals []interval
	for _, line := range strings.Split(strings.TrimSpace(ranges), "\n") {
		parts := strings.Split(line, "-")
		start, _ := strconv.ParseInt(strings.TrimSpace(parts[0]), 10, 64)
		end, _ := strconv.ParseInt(strings.TrimSpace(parts[1]), 10, 64)
		intervals = append(intervals, interval{start: start, end: end})
	}
	slices.SortFunc(intervals, func(a, b interval) int {
		if a.start != b.start {
			return int(a.start - b.start)
		}
		return int(a.end - b.end)
	})

	//merge intervals
	var merged []interval
	for i := 0; i < len(intervals); i++ {
		if i == 0 || intervals[i].start > merged[len(merged)-1].end {
			merged = append(merged, intervals[i])
		} else {
			merged[len(merged)-1].end = max(merged[len(merged)-1].end, intervals[i].end)
		}
	}

	// Parse ingredients
	var ingList []int64
	for _, line := range strings.Split(strings.TrimSpace(ingredients), "\n") {
		val, _ := strconv.ParseInt(strings.TrimSpace(line), 10, 64)
		ingList = append(ingList, val)
	}
	slices.Sort(ingList)

	fresh := 0
	for _, v := range ingList {
		// binary search in merged intervals
		_, found := slices.BinarySearchFunc(merged, interval{start: v, end: v}, func(a, b interval) int {
			if a.end < b.start {
				return -1
			} else if a.start > b.end {
				return 1
			}
			return 0
		})
		if found {
			fresh++
		}
	}

	/* 	fmt.Printf("Parsed intervals: %+v\n", merged)
	   	fmt.Printf("Parsed ingredients: %+v\n", ingList) */

	return fresh
}

func part2(input string) int {
	input = strings.ReplaceAll(input, "\r\n", "\n")
	split := strings.Split(input, "\n\n")

	ranges, _ := split[0], split[1]

	// Parse ranges
	var intervals []interval
	for _, line := range strings.Split(strings.TrimSpace(ranges), "\n") {
		parts := strings.Split(line, "-")
		start, _ := strconv.ParseInt(strings.TrimSpace(parts[0]), 10, 64)
		end, _ := strconv.ParseInt(strings.TrimSpace(parts[1]), 10, 64)
		intervals = append(intervals, interval{start: start, end: end})
	}
	slices.SortFunc(intervals, func(a, b interval) int {
		if a.start != b.start {
			return int(a.start - b.start)
		}
		return int(a.end - b.end)
	})

	//merge intervals
	var merged []interval
	for i := 0; i < len(intervals); i++ {
		if i == 0 || intervals[i].start > merged[len(merged)-1].end {
			merged = append(merged, intervals[i])
		} else {
			merged[len(merged)-1].end = max(merged[len(merged)-1].end, intervals[i].end)
		}
	}

	total := int64(0)
	for _, iv := range merged {
		total += iv.end - iv.start + 1
	}

	return int(total)
}
