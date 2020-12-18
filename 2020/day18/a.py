def read_input():
    with open("../inputs/day18.txt") as f:
        info = ["(" + l.strip().replace(" ", "") + ")" for l in f]
    return info


import math
from collections import defaultdict, deque
from copy import deepcopy
import itertools

ops = ["(", ")", "+", "*"]


def single(line):
    parens = deque()
    for c in line:
        if c != ")":
            parens.append(c)
        else:
            operation = []
            while parens[-1] != "(":
                op = parens.pop()
                operation.append(op)
            parens.pop()
            operation = operation[::-1]
            mini, idx = int(operation[0]), 1
            while idx < len(operation):
                o = operation[idx]
                if o == "+":
                    idx += 1
                    num = int(operation[idx])
                    mini += num
                elif o == "*":
                    idx += 1
                    num = int(operation[idx])
                    mini *= num
                else:
                    print("unreachable", operation, o)
                idx += 1
            parens.append(str(mini))
    return int(parens[0])


def adv_single(line):
    parens = deque()
    for c in line:
        if c != ")":
            # handle addition
            if len(parens) > 0 and parens[-1] == "+" and c.isdigit():
                parens.pop()
                n = parens.pop()
                parens.append(str(int(c) + int(n)))
            else:
                parens.append(c)
        else:
            operation = []
            while parens[-1] != "(":
                op = parens.pop()
                operation.append(op)
            parens.pop()
            operation = operation[::-1]
            mini, idx = int(operation[0]), 1
            while idx < len(operation):
                o = operation[idx]
                if o == "+":
                    idx += 1
                    num = int(operation[idx])
                    mini += num
                elif o == "*":
                    idx += 1
                    num = int(operation[idx])
                    mini *= num
                else:
                    print("unreachable", operation, o)
                idx += 1
            # handle addition
            if len(parens) > 0 and parens[-1] == "+":
                parens.pop()
                n = parens.pop()
                mini += int(n)
                while len(parens) > 0 and parens[-1] == "+":
                    parens.pop()
                    n = parens.pop()
                    mini += int(n)
            parens.append(str(mini))

    return int(parens[0])


def eval(lines):
    res = []
    adv = []
    for l in lines:
        res.append(single(l))
        adv.append(adv_single(l))

    print(sum(res))
    print(sum(adv))


inputs = read_input()
eval(inputs)
