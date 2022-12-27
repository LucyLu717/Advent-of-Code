import numpy as np
import itertools


def get_combo(edges):
    combo = []
    for i in range(4):
        new = np.roll(edges, i)
        combo.append(new)
        new1 = [new[2], new[1][::-1], new[0], new[3][::-1]]
        combo.append(new1)
        new2 = [new[0][::-1], new[3], new[2][::-1], new[1]]
        combo.append(new2)
    return np.array(combo)


def read_input():
    with open("../inputs/day20.txt") as f:
        tiles = {}
        inputs = [tile.split(":") for tile in f.read().split("\n\n")]
        for i in inputs:
            t = np.array([np.array(list(l)) for l in i[1].strip().split("\n")])
            edges = np.array([t[0], t[:, -1], t[-1], t[:, 0]])
            tiles[int(i[0][-4:])] = get_combo(edges)
    return tiles


def rearrange(tiles):
    overlaps = {t: set() for t in tiles.keys()}
    for t, combo in tiles.items():
        print(t)
        for o in combo:
            for i, e in enumerate(o):
                for t2, combo2 in tiles.items():
                    if t != t2:
                        for o in combo2:
                            if (o[i - 2] == e).all():
                                overlaps[t].add(t2)
    print(overlaps)


tiles = read_input()
rearrange(tiles)
