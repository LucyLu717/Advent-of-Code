def read_input():
    with open("../inputs/day13.txt") as f:
        info = [line.strip() for line in f]
        arrival = float(info[0])
        buses_x = info[1].split(",")
        buses = [int(b) for b in info[1].replace("x,", "").split(",")]
    return arrival, buses, buses_x


import math
import numpy as np


def departure(arrival, buses):
    earliest = [0] * len(buses)
    for i, b in enumerate(buses):
        earliest[i] = math.ceil(arrival / b) * b
    t = np.argmin(earliest)
    print((earliest[t] - arrival) * buses[t])


"""
    Source: https://github.com/TheAlgorithms/Python/blob/master/blockchain/chinese_remainder_theorem.py
"""
# Extended Euclid
def extended_euclid(a, b):
    """
    >>> extended_euclid(10, 6)
    (-1, 2)
    >>> extended_euclid(7, 5)
    (-2, 3)
    """
    if b == 0:
        return (1, 0)
    (x, y) = extended_euclid(b, a % b)
    k = a // b
    return (y, x - k * y)


# Uses ExtendedEuclid to find inverses
def chinese_remainder_theorem(n1, r1, n2, r2):
    """
    >>> chinese_remainder_theorem(5,1,7,3)
    31
    Explanation : 31 is the smallest number such that
                (i)  When we divide it by 5, we get remainder 1
                (ii) When we divide it by 7, we get remainder 3
    >>> chinese_remainder_theorem(6,1,4,3)
    14
    """
    (x, y) = extended_euclid(n1, n2)
    m = n1 * n2
    n = r2 * x * n1 + r1 * y * n2
    return (n % m + m) % m


def get_n(inputs):
    d = []
    for pos, i in enumerate(inputs):
        if i != "x":
            d.append((int(i), pos))
    n = chinese_remainder_theorem(d[0][0], d[0][1], d[1][0], d[1][1])
    prod = d[1][0] * d[0][0]
    for i in range(2, len(d)):
        n = chinese_remainder_theorem(d[i][0], d[i][1], prod, n)
        prod *= d[i][0]
    print(prod - n)  # works because it's -i mod m


arrival, buses, inputs = read_input()
departure(arrival, buses)
get_n(inputs)

