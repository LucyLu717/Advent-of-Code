import sys


dir_map = {'R': (1, 0), 'L': (-1, 0), 'U': (0, 1), 'D': (0, -1)}


def main():
    with open("input.txt") as file:
        wire1 = file.readline()
        wire2 = file.readline()
    wire1 = get_dir_len(wire1)
    wire2 = get_dir_len(wire2)
    points1 = get_points(wire1)
    points2 = get_points(wire2)
    min_dis = sys.maxsize

    part_one(points1, points2, min_dis)
    part_two(wire1, wire2, points1, points2, min_dis)


def part_one(points1, points2, min_dis):
    min_x = 0
    min_y = 0
    for point in points1:
        if point in points2:
            x = abs(point[0])
            y = abs(point[1])
            if x + y < min_dis:
                min_x = x
                min_y = y
                min_dis = x + y
    print(min_x + min_y)

def part_two(wire1, wire2, points1, points2, min_dis):
    for point in points1:
        if point in points2:
            step1 = calc_step(wire1, point)
            step2 = calc_step(wire2, point)
            if step1 == -1 or step2 == -1:
                print(step1, step2, " something's wrong")
            if step1 + step2 < min_dis:
                min_dis = step1 + step2
    print(min_dis)



def get_dir_len(line):
    wire_arr = line.split(",")
    wire = []
    for s in wire_arr:
        dir = s[0]
        length = int(s[1:])
        wire.append((dir, length))
    return wire


def get_points(wire):
    points = set()
    last_x = 0
    last_y = 0
    for side in wire:
        dir_x, dir_y = get_dir_xy(side[0])
        length = side[1]

        for i in range(1, length + 1):
            points.add((last_x + i * dir_x, last_y + i * dir_y))
        last_x = last_x + length * dir_x
        last_y = last_y + length * dir_y

    return points


def calc_step(wire, point):
    last_x = 0
    last_y = 0
    step = 1
    for side in wire:
        dir_x, dir_y = get_dir_xy(side[0])
        length = side[1]

        for i in range(1, length + 1):
            if point != (last_x + i * dir_x, last_y + i * dir_y):
                step += 1
            else:
                return step
        last_x = last_x + length * dir_x
        last_y = last_y + length * dir_y

    return -1


def get_dir_xy(dir):
    dir_xy = dir_map.get(dir)
    return dir_xy[0], dir_xy[1]


if __name__ == '__main__':
    main()