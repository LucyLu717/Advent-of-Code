def read_input():
    lines = []
    with open("../inputs/day2.txt") as f:
        for line in f:
            lines.append(line.strip())
    return lines


def check_helper(line):
    parts = line.split(" ")
    range_ = parts[0].split("-")
    min_ = int(range_[0])
    max_ = int(range_[1])
    letter = parts[1][0]
    pwd = parts[-1]
    count = pwd.count(letter)
    if min_ <= count <= max_:
        return True
    else:
        return False


def new_check_helper(line):
    parts = line.split(" ")
    range_ = parts[0].split("-")
    pos1 = int(range_[0])
    pos2 = int(range_[1])
    letter = parts[1][0]
    pwd = parts[-1]
    return (
        pwd[pos1 - 1] == letter
        and not pwd[pos2 - 1] == letter
        or not pwd[pos1 - 1] == letter
        and pwd[pos2 - 1] == letter
    )


def check(lines, part):
    count = 0
    for line in lines:
        if part(line):
            count += 1
    print(count)


def main():
    lines = read_input()
    check(lines, check_helper)
    check(lines, new_check_helper)


if __name__ == "__main__":
    main()
