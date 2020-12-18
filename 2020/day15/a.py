def read_input():
    with open("../inputs/day15.txt") as f:
        info = [line.strip() for line in f][0]
        info = [int(n) for n in info.split(",")]
    return info


from collections import defaultdict


def take_turns(numbers, cutoff):
    history = defaultdict(tuple)
    turns = 0
    for n in numbers:
        turns += 1
        history[n] = (turns, -1)
    last = numbers[-1]
    while turns < cutoff:
        turns += 1
        if history[last][1] == -1:
            last = 0
            if 0 not in history:
                history[0] = (turns, -1)
            elif history[0][1] != -1:
                history[0] = (history[0][1], turns)
            else:
                history[0] = (history[0][0], turns)
        else:
            last = history[last][1] - history[last][0]
            if last not in history:
                history[last] = (turns, -1)
            elif history[last][1] != -1:
                history[last] = (history[last][1], turns)
            else:
                history[last] = (history[last][0], turns)
        if turns % 1000000 == 0:
            print(last, turns)
    print(last)


inputs = read_input()
take_turns(inputs, 2020)
take_turns(inputs, 30000000)
