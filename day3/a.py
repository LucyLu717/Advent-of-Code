def read_input():
    lines = []
    with open("../inputs/day3.txt") as f:
        for line in f:
            lines.append(line.strip())
    return lines


def count_trees(lines, down, right):
    trees = 0
    length = len(lines[0])
    for idx, line in enumerate(lines):
        if idx % down == 0 and line[idx / down * right % length] == "#":
            trees += 1
    return trees


"""
Right 1, down 1.
Right 3, down 1. (This is the slope you already checked.)
Right 5, down 1.
Right 7, down 1.
Right 1, down 2.
"""


def count_total(lines, pos):
    trees = 1
    for p in pos:
        trees *= count_trees(lines, p[0], p[1])
    print(trees)


def main():
    lines = read_input()
    pos = [[1, 1], [1, 3], [1, 5], [1, 7], [2, 1]]
    count_total(lines, [pos[1]])
    count_total(lines, pos)


if __name__ == "__main__":
    main()
