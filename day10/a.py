import math


def read_input():
    with open("../inputs/day10.txt") as f:
        numbers = [int(line.strip()) for line in f]
    return numbers


def find_joltage(numbers):
    difference = {1: 0, 2: 0, 3: 0}
    joltage = 0
    maxj = max(numbers)
    while joltage < maxj:
        passa = []
        for j in [1, 2, 3]:
            if joltage + j in numbers:
                passa.append(j)
        n = min(passa)
        difference[n] += 1
        joltage += n
    print(difference[1] * (difference[3] + 1))


def find_ways(numbers):
    numbers.append(0)
    ways = [0] * (max(numbers) + 1)
    ways[0] = 1
    numbers = sorted(numbers)
    for i in numbers:
        for j in [1, 2, 3]:
            if i - j in numbers:
                ways[i] += ways[i - j]
    print(ways[-1])


def main():
    numbers = read_input()
    find_joltage(numbers)
    find_ways(numbers)


if __name__ == "__main__":
    main()
