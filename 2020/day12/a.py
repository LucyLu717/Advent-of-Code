def read_input():
    with open("../inputs/day12.txt") as f:
        insn = [(line.strip()[0], int(line.strip()[1:])) for line in f]
    return insn


dirs = ["E", "S", "W", "N"]


def step(letter, num, dist, curr_dir):
    if letter == "L":
        curr_dir = dirs[(dirs.index(curr_dir) - num // 90 + 4) % 4]
    elif letter == "R":
        curr_dir = dirs[(dirs.index(curr_dir) + num // 90 - 4) % 4]
    else:
        if letter == "F":
            letter = curr_dir
        oppo_dir = dirs[(dirs.index(letter) + 2) % 4]
        if dist[oppo_dir] != 0:
            dist_in_curr = num - dist[oppo_dir]
            if dist_in_curr < 0:
                dist[oppo_dir] = -dist_in_curr
            else:
                dist[letter] = dist_in_curr
                dist[oppo_dir] = 0
        else:
            dist[letter] += num
    return curr_dir


def move(insn):
    dist = dict(zip(dirs, [0] * 4))
    curr_dir = dirs[0]
    for letter, num in insn:
        curr_dir = step(letter, num, dist, curr_dir)
    print(sum(dist.values()))


def move_way(insn):
    dist = dict(zip(dirs, [0] * 4))
    curr_dir = dirs[0]

    dist_way = dict(zip(dirs, [0] * 4))
    dist_way["E"] = 10
    dist_way["N"] = 1

    for letter, num in insn:
        if letter == "L":
            new_dist_way = [dist_way[dirs[(i + num // 90 - 4) % 4]] for i in range(4)]
            dist_way = dict(zip(dirs, new_dist_way))
        elif letter == "R":
            new_dist_way = [dist_way[dirs[(i - num // 90 + 4) % 4]] for i in range(4)]
            dist_way = dict(zip(dirs, new_dist_way))
        elif letter == "F":
            for i, d in enumerate(dirs):
                if dist_way[d] != 0:
                    for _ in range(num):
                        curr_dir = step(d, dist_way[d], dist, curr_dir)
        else:
            step(letter, num, dist_way, curr_dir)
    print(sum(dist.values()))


insn = read_input()
move(insn)
move_way(insn)

