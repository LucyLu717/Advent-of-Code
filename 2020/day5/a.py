def read_input():
    lines = []
    with open("../inputs/day5.txt") as f:
        for line in f:
            lines.append(line.strip())
    return lines


def get_binary(bi):
    return int(bi, 2)


def seat(line):
    row = ["0" if x == "F" else "1" for x in line[:-3]]
    col = ["0" if x == "L" else "1" for x in line[-3:]]
    row = get_binary("".join(row))
    col = get_binary("".join(col))
    return row * 8 + col


def find_highest(lines):
    highest = max(seat(l) for l in lines)
    print(highest)
    return highest


def find_id(lines, highest):
    ids = set()
    for line in lines:
        seat_num = seat(line)
        ids.add(seat_num)
    for i in range(1, highest):
        if i - 1 in ids and i + 1 in ids and i not in ids:
            print(i)


def main():
    lines = read_input()
    highest = find_highest(lines)
    find_id(lines, highest)


if __name__ == "__main__":
    main()
