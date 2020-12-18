import numpy as np


POS = [[14, 4, 5], [12, 10, 8], [1, 7, -10], [16, -5, 3]]
# test: POS = [[-1, 0, 2], [2, -10, -7], [4, -8, 8], [3, 5, -1]]
VEL = [[0, 0, 0], [0, 0, 0], [0, 0, 0], [0, 0, 0]]
ENERGY = [0] * 4

SET = [set(), set(), set()]
STEPS = [0, 0, 0]


def main():
    part_one()
    part_two()


def update_velocity():
    for i in range(4):
        for j in range(4):
            if i != j:
                for dim in range(3):
                    VEL[i][dim] += -1 if POS[i][dim] > POS[j][dim] else \
                        (1 if POS[i][dim] < POS[j][dim] else 0)


def update_position():
    for i in range(4):
        for dim in range(3):
            POS[i][dim] += VEL[i][dim]


def calc_energy():
    for i in range(4):
        potential = 0
        kinetic = 0
        for dim in range(3):
            potential += abs(POS[i][dim])
            kinetic += abs(VEL[i][dim])
        ENERGY[i] = potential * kinetic


def part_one():
    for step in range(1000):
        update_velocity()
        update_position()
    calc_energy()
    print(sum(ENERGY))


def convert_list_to_str(dim):
    pos = ""
    vel = ""
    for i in range(4):
        pos += str(POS[i][dim])
        vel += str(VEL[i][dim])
    return pos, vel


def update_velocity_dim(dim):
    for i in range(4):
        for j in range(4):
            if i != j:
                VEL[i][dim] += -1 if POS[i][dim] > POS[j][dim] else \
                    (1 if POS[i][dim] < POS[j][dim] else 0)


def update_position_dim(dim):
    for i in range(4):
        POS[i][dim] += VEL[i][dim]


def part_two():
    for i in range(3):
        step = 0
        while True:
            update_velocity_dim(i)
            update_position_dim(i)
            pos, vel = convert_list_to_str(i)
            if (pos, vel) in SET[i]:
                STEPS[i] = step
                break
            else:
                SET[i].add((pos, vel))
            step += 1
    print(STEPS)
    print(np.lcm.reduce(STEPS))


if __name__ == '__main__':
    main()
