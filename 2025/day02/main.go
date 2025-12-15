package main

import (
	"fmt"
	"os"
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

func part1(input string) int {
	invalids := 0
	for _, rangeToken := range strings.Split(strings.TrimSpace(input), ",") {
		bounds := strings.Split(rangeToken, "-")

		start, _ := strconv.Atoi(bounds[0])
		end, _ := strconv.Atoi(bounds[1])

		// only even digit subranges can have a period of 2
		for _, evenRange := range rangesWithEvenDigits(start, end) {
			start, end := evenRange[0], evenRange[1]
			for num := start; num <= end; num++ {
				str := strconv.Itoa(num)
				if len(str)%2 != 0 {
					continue
				}
				half := len(str) / 2
				if str[half:] == str[:half] {
					invalids += num
				}
			}
		}
	}
	return invalids
}

func part2(input string) int {

	invalids := 0
	for _, rangeToken := range strings.Split(strings.TrimSpace(input), ",") {
		bounds := strings.Split(rangeToken, "-")

		start, _ := strconv.Atoi(bounds[0])
		end, _ := strconv.Atoi(bounds[1])

		for num := start; num <= end; num++ {
			str := strconv.Itoa(num)
			if len(str) < 2 {
				continue
			}

			ss := str + str
			// search for s inside ss with first and last character removed to avoid the trivial match.
			if strings.Contains(ss[1:len(ss)-1], str) {
				invalids += num
			}
		}
	}
	return invalids
}

func rangesWithEvenDigits(start, end int) [][2]int {
	if start > end {
		start, end = end, start
	}
	ranges := make([][2]int, 0)

	maxDigits := len(strconv.Itoa(end))
	pow10Int := func(n int) int {
		power := 1
		for i := 0; i < n; i++ {
			power *= 10
		}
		return power
	}
	for digitCount := 1; digitCount <= maxDigits; digitCount++ {
		bucketStart := pow10Int(digitCount - 1)
		bucketEnd := pow10Int(digitCount) - 1
		// Intersect with [start,end]
		interStart := max(start, bucketStart)
		interEnd := min(end, bucketEnd)
		if interStart <= interEnd && digitCount%2 == 0 {
			ranges = append(ranges, [2]int{interStart, interEnd})
		}
	}
	return ranges
}
