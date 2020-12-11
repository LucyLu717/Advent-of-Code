DIRS = [(-1, -1), (-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1)]
import copy


def read_input():
    with open("../inputs/day11.txt") as f:
        layout = [list(line.strip().replace("L", "#")) for line in f]
    return layout


def count_occupied(layout):
    o = sum([l.count("#") for l in layout])
    print(o)


# Over engineered
# def find_dir(r, c, layout):
#     if r == 0:
#         if c == 0:
#             return []  # 4, 6, 7
#         elif c == len(layout[0]) - 1:
#             return []  # 3, 5, 6
#         else:
#             return [3, 4, 5, 6, 7]
#     elif r == len(layout) - 1:
#         if c == 0:
#             return []  # 1, 2, 4
#         elif c == len(layout[0]) - 1:
#             return []  # 0, 1, 3
#         else:
#             return [0, 1, 2, 3, 4]
#     else:
#         if c == 0:
#             return [1, 2, 4, 6, 7]
#         elif c == len(layout[0]) - 1:
#             return [0, 1, 3, 5, 6]
#         else:
#             return list(range(7))


def check_seat(r, c, layout):
    occupied = 0
    for dir in DIRS:
        new_r = r + dir[0]
        new_c = c + dir[1]
        if (
            0 <= new_r < len(layout)
            and 0 <= new_c < len(layout[0])
            and layout[r + dir[0]][c + dir[1]] == "#"
        ):
            occupied += 1
    if layout[r][c] == "L" and occupied == 0:
        return "#"
    elif layout[r][c] == "#" and occupied >= 4:
        return "L"
    return layout[r][c]


def check_seat2(r, c, layout):
    occupied = 0
    for dir in DIRS:
        new_r = r + dir[0]
        new_c = c + dir[1]
        while 0 <= new_r < len(layout) and 0 <= new_c < len(layout[0]):
            if layout[new_r][new_c] == "#":
                occupied += 1
                break
            if layout[new_r][new_c] == "L":
                break
            new_r += dir[0]
            new_c += dir[1]
    if layout[r][c] == "L" and occupied == 0:
        return "#"
    elif layout[r][c] == "#" and occupied >= 5:
        return "L"
    return layout[r][c]


def apply(layout, f):
    prev = [[]]
    while prev != layout:
        prev = copy.deepcopy(layout)
        for r in range(len(layout)):
            for c in range(len(layout[0])):
                layout[r][c] = f(r, c, prev)
    count_occupied(layout)


def main():
    layout = read_input()
    apply(layout, check_seat)
    layout = read_input()
    apply(layout, check_seat2)


if __name__ == "__main__":
    main()
