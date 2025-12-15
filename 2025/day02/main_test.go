package main

import (
	"testing"
)

func TestPart1(t *testing.T) {
	sampleInput := `11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124`

	got := part1(sampleInput)
	want := 1227775554

	if got != want {
		t.Errorf("part1() = %d; want %d", got, want)
	}
}
func TestPart2(t *testing.T) {
	sampleInput := `11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124`

	got := part2(sampleInput)
	want := 4174379265

	if got != want {
		t.Errorf("part2() = %d; want %d", got, want)
	}
}
