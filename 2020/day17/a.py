def read_input():
    with open("../inputs/day17.txt") as f:
        info = [list(l.strip()) for l in f]
    return info


import itertools
from collections import defaultdict
from copy import deepcopy

dir = [[0, 1, -1]] * 3
directions = [e for e in itertools.product(*dir)]
directions.remove((0, 0, 0))


def sixcycle(inputs):
    cube = defaultdict(list)
    for r, l in enumerate(inputs):
        for c, p in enumerate(l):
            if p == "#":
                cube[0].append((r, c))
    cube[-1] = []
    cube[1] = []
    minsq = -1
    maxsq = len(inputs)
    for round in range(6):
        new_cube = deepcopy(cube)
        for l, layer in cube.items():
            for r in range(minsq, maxsq + 1):
                for c in range(minsq, maxsq + 1):
                    active = 0
                    for dir in directions:
                        new_r = r + dir[0]
                        new_c = c + dir[1]
                        new_l = l + dir[2]
                        if (new_r, new_c) in cube[new_l]:
                            active += 1
                    if (r, c) in cube[l]:
                        if not (active == 2 or active == 3):
                            new_cube[l].remove((r, c))
                    else:
                        if active == 3:
                            new_cube[l].append((r, c))
        minsq -= 1
        maxsq += 1
        new_cube[round + 2] = []
        new_cube[-(round + 2)] = []
        cube = new_cube
    print(sum(len(v) for v in cube.values()))


dir4 = [[0, 1, -1]] * 4
directions4 = [e for e in itertools.product(*dir4)]
directions4.remove((0, 0, 0, 0))


def sixcycle_fourdim(inputs):
    cube = defaultdict(list)
    for r, l in enumerate(inputs):
        for c, p in enumerate(l):
            if p == "#":
                cube[(0, 0)].append((r, c))
    for i in range(-1, 2):
        for j in range(-1, 2):
            if (i, j) not in cube:
                cube[(i, j)] = []
    minsq = -1
    maxsq = len(inputs)
    for round in range(6):
        new_cube = deepcopy(cube)
        for l, layer in cube.items():
            for r in range(minsq, maxsq + 1):
                for c in range(minsq, maxsq + 1):
                    active = 0
                    for dir in directions4:
                        new_r = r + dir[0]
                        new_c = c + dir[1]
                        new_z = l[0] + dir[2]
                        new_w = l[1] + dir[3]
                        if (new_r, new_c) in cube[(new_z, new_w)]:
                            active += 1
                    if (r, c) in cube[l]:
                        if not (active == 2 or active == 3):
                            new_cube[l].remove((r, c))
                    else:
                        if active == 3:
                            new_cube[l].append((r, c))
        minsq -= 1
        maxsq += 1
        for i in range(-(round + 2), round + 3):
            for j in range(-(round + 2), round + 3):
                if (i, j) not in new_cube:
                    new_cube[(i, j)] = []
        cube = new_cube
    print(sum(len(v) for v in cube.values()))


inputs = read_input()
sixcycle(inputs)
sixcycle_fourdim(inputs)
